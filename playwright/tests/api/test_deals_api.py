from playwright.sync_api import APIRequestContext

from data.deal_test_data import newDeal


def test_deals_api_supports_create_read_update_and_delete(
    apiRequestContext: APIRequestContext,
) -> None:
    deal = newDeal()
    dealId = None
    try:
        created = apiRequestContext.post("/api/deals", data=deal.apiPayload())
        assert created.status == 201
        createdBody = created.json()
        dealId = createdBody["id"]
        assert createdBody["company"] == deal.company
        assert createdBody["stageCode"] == "REGISTRATION"

        retrieved = apiRequestContext.get(f"/api/deals/{dealId}")
        assert retrieved.status == 200
        assert retrieved.json()["id"] == dealId
        assert retrieved.json()["company"] == deal.company

        updatePayload = deal.apiPayload() | {
            "company": f"{deal.company} Updated",
            "value": 125_000,
            "probability": 20,
        }
        updated = apiRequestContext.put(f"/api/deals/{dealId}", data=updatePayload)
        assert updated.status == 200
        assert updated.json()["company"] == updatePayload["company"]
        assert updated.json()["value"] == 125_000
        assert updated.json()["probability"] == 20

        deleted = apiRequestContext.delete(f"/api/deals/{dealId}")
        assert deleted.status == 204
        notFound = apiRequestContext.get(f"/api/deals/{dealId}")
        assert notFound.status == 404
        assert notFound.json()["message"] == "Deal not found."
        dealId = None
    finally:
        if dealId is not None:
            apiRequestContext.delete(f"/api/deals/{dealId}")


def test_move_api_rejects_registration_without_positive_budget(
    apiRequestContext: APIRequestContext,
) -> None:
    deal = newDeal(value=0)
    created = apiRequestContext.post("/api/deals", data=deal.apiPayload())
    assert created.status == 201
    dealId = created.json()["id"]

    try:
        moved = apiRequestContext.patch(
            f"/api/deals/{dealId}/move",
            data={"direction": "next", "username": "Admin", "password": "Admin"},
        )
        assert moved.status == 400
        assert moved.json()["message"] == (
            "Registration to Access requires customer information and a budget value."
        )

        retrieved = apiRequestContext.get(f"/api/deals/{dealId}")
        assert retrieved.status == 200
        assert retrieved.json()["stageCode"] == "REGISTRATION"
        assert retrieved.json()["probability"] == 10
    finally:
        apiRequestContext.delete(f"/api/deals/{dealId}")
