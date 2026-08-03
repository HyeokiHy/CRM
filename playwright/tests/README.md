# Opportunity E2E test skeleton

현재 추가된 코드는 Playwright Python + pytest 테스트의 공통 뼈대뿐입니다.
완성된 validation이나 단계 전환 시나리오는 포함하지 않으며, Registration
validation부터 직접 구현합니다.

애플리케이션을 `http://localhost:8081`에서 실행한 뒤 저장소 루트에서 실행합니다.

```powershell
python -m pip install -r playwright/requirements.txt
python -m playwright install chromium
python -m pytest
```

headed 실행:

```powershell
python -m pytest --headed
```

TODO 순서:

1. Registration required validation
2. Registration valid creation
3. Registration -> Access validation
4. Remaining stage transitions
5. Full Registration -> Closed happy path
