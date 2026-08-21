from playwright.sync_api import APIRequestContext

from data.opportunity_data import newOpportunity


def test_deals_api_supports_create_read_update_and_delete(
    apiRequestContext: APIRequestContext,
) -> None:
    opportunity = newOpportunity()
    dealId = None
    try:
        created = apiRequestContext.post("/api/deals", data=opportunity.apiPayload())
        assert created.status == 201
        createdBody = created.json()
        dealId = createdBody["id"]
        assert createdBody["company"] == opportunity.company
        assert createdBody["stageCode"] == "REGISTRATION"

        retrieved = apiRequestContext.get(f"/api/deals/{dealId}")
        assert retrieved.status == 200
        assert retrieved.json()["id"] == dealId
        assert retrieved.json()["company"] == opportunity.company

        updatePayload = opportunity.apiPayload() | {
            "company": f"{opportunity.company} Updated",
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
    opportunity = newOpportunity(value=0)
    created = apiRequestContext.post("/api/deals", data=opportunity.apiPayload())
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
