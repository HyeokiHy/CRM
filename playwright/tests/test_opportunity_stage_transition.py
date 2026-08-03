import pytest

from data.opportunity_data import OPPORTUNITY_STAGES


@pytest.mark.skip(
    reason="TODO: Implement stage-transition validation manually.",
)
def test_opportunity_stage_transition_skeleton() -> None:
    # TODO: Select only the transition cases being implemented from
    # OPPORTUNITY_STAGES after deciding how each opportunity will be isolated.
    # TODO: Attempt one confirmed transition and assert its validation and state.
    pytest.fail(
        f"TODO: Stage-transition tests are not implemented for "
        f"{len(OPPORTUNITY_STAGES)} confirmed stages.",
    )
