const stages = [
  {
    name: "Registration",
    code: "REGISTRATION",
    description: "New account and opportunity intake.",
    probability: 10,
  },
  {
    name: "Access",
    code: "ACCESS",
    description: "Stakeholder access and discovery.",
    probability: 45,
  },
  {
    name: "Go - No Go",
    code: "GO_NO_GO",
    description: "Qualification and bid decision.",
    probability: 25,
  },
  {
    name: "Award",
    code: "AWARD",
    description: "Preferred bidder or final negotiation.",
    probability: 75,
  },
  {
    name: "Closed",
    code: "CLOSED",
    description: "Won, lost, or formally closed.",
    probability: 100,
  },
];

const translations = {
  en: {
    appTitle: "Deal Pipeline",
    language: "Language",
    currentUser: "Current user",
    user: "User",
    password: "Password",
    adminPlaceholder: "Admin",
    createUser: "Create User",
    pipelineSummary: "Pipeline summary",
    totalDeals: "Total deals",
    pipelineValue: "Pipeline value",
    weightedForecast: "Weighted forecast",
    activeAccounts: "Active accounts",
    pipelineControls: "Pipeline controls",
    search: "Search",
    searchPlaceholder: "Company, owner, next action",
    stage: "Stage",
    allStages: "All stages",
    priority: "Priority",
    allPriorities: "All priorities",
    dealPipeline: "Deal pipeline",
    opportunity: "Opportunity",
    close: "Close",
    company: "Company",
    contact: "Contact",
    owner: "Owner",
    value: "Value",
    probability: "Probability",
    closeDate: "Close date",
    opportunityLocation: "Opportunity location",
    expectedItems: "Items or materials",
    nextAction: "Next action",
    delete: "Delete",
    cancel: "Cancel",
    saveDeal: "Save Deal",
    newDeal: "New Deal",
    editDeal: "Edit Deal",
    noDeals: "No deals",
    prev: "Prev",
    next: "Next",
    loadError: "Could not load CRM data. Check that Spring Boot is running.",
    createUserRequired: "Enter a username and password before creating a user.",
    userCreated: (username) => `User ${username} was created.`,
    createUserError: "Could not create user.",
    saveDealError: "Could not save deal. Check the server and input values.",
    deleteDealError: "Could not delete deal.",
    stages: {
      REGISTRATION: {
        name: "Registration",
        description: "New account and opportunity intake.",
      },
      ACCESS: {
        name: "Access",
        description: "Stakeholder access and discovery.",
      },
      GO_NO_GO: {
        name: "Go - No Go",
        description: "Qualification and bid decision.",
      },
      AWARD: {
        name: "Award",
        description: "Preferred bidder or final negotiation.",
      },
      CLOSED: {
        name: "Closed",
        description: "Won, lost, or formally closed.",
      },
    },
    priorities: {
      HIGH: "High",
      MEDIUM: "Medium",
      LOW: "Low",
    },
  },
  ko: {
    appTitle: "영업 파이프라인",
    language: "언어",
    currentUser: "현재 사용자",
    user: "사용자",
    password: "비밀번호",
    adminPlaceholder: "관리자",
    createUser: "사용자 생성",
    pipelineSummary: "파이프라인 요약",
    totalDeals: "전체 거래",
    pipelineValue: "파이프라인 금액",
    weightedForecast: "가중 예상 금액",
    activeAccounts: "활성 고객사",
    pipelineControls: "파이프라인 컨트롤",
    search: "검색",
    searchPlaceholder: "회사, 담당자, 다음 액션",
    stage: "단계",
    allStages: "전체 단계",
    priority: "우선순위",
    allPriorities: "전체 우선순위",
    dealPipeline: "거래 파이프라인",
    opportunity: "영업 기회",
    close: "닫기",
    company: "회사",
    contact: "연락처",
    owner: "담당자",
    value: "금액",
    probability: "확률",
    closeDate: "마감일",
    opportunityLocation: "기회 지역",
    expectedItems: "품목 또는 자재",
    nextAction: "다음 액션",
    delete: "삭제",
    cancel: "취소",
    saveDeal: "거래 저장",
    newDeal: "새 거래",
    editDeal: "거래 수정",
    noDeals: "거래 없음",
    prev: "이전",
    next: "다음",
    loadError: "CRM 데이터를 불러오지 못했습니다. Spring Boot가 실행 중인지 확인하세요.",
    createUserRequired: "사용자 생성 전에 사용자명과 비밀번호를 입력하세요.",
    userCreated: (username) => `${username} 사용자를 생성했습니다.`,
    createUserError: "사용자를 생성하지 못했습니다.",
    saveDealError: "거래를 저장하지 못했습니다. 서버와 입력값을 확인하세요.",
    deleteDealError: "거래를 삭제하지 못했습니다.",
    stages: {
      REGISTRATION: {
        name: "등록",
        description: "신규 고객사와 영업 기회를 접수합니다.",
      },
      ACCESS: {
        name: "접근",
        description: "이해관계자 접점과 니즈를 확인합니다.",
      },
      GO_NO_GO: {
        name: "진행 판단",
        description: "자격 요건과 입찰 진행 여부를 결정합니다.",
      },
      AWARD: {
        name: "수주",
        description: "우선협상 또는 최종 협상을 진행합니다.",
      },
      CLOSED: {
        name: "종료",
        description: "수주, 실주 또는 공식 종료 상태입니다.",
      },
    },
    priorities: {
      HIGH: "높음",
      MEDIUM: "보통",
      LOW: "낮음",
    },
  },
  ja: {
    appTitle: "営業パイプライン",
    language: "言語",
    currentUser: "現在のユーザー",
    user: "ユーザー",
    password: "パスワード",
    adminPlaceholder: "管理者",
    createUser: "ユーザー作成",
    pipelineSummary: "パイプライン概要",
    totalDeals: "案件数",
    pipelineValue: "パイプライン金額",
    weightedForecast: "加重予測金額",
    activeAccounts: "有効な顧客",
    pipelineControls: "パイプライン操作",
    search: "検索",
    searchPlaceholder: "会社、担当者、次のアクション",
    stage: "ステージ",
    allStages: "すべてのステージ",
    priority: "優先度",
    allPriorities: "すべての優先度",
    dealPipeline: "案件パイプライン",
    opportunity: "商談",
    close: "閉じる",
    company: "会社",
    contact: "連絡先",
    owner: "担当者",
    value: "金額",
    probability: "確度",
    closeDate: "完了予定日",
    opportunityLocation: "商談地域",
    expectedItems: "品目または資材",
    nextAction: "次のアクション",
    delete: "削除",
    cancel: "キャンセル",
    saveDeal: "案件を保存",
    newDeal: "新規案件",
    editDeal: "案件を編集",
    noDeals: "案件なし",
    prev: "前へ",
    next: "次へ",
    loadError: "CRMデータを読み込めませんでした。Spring Bootが起動しているか確認してください。",
    createUserRequired: "ユーザー作成前にユーザー名とパスワードを入力してください。",
    userCreated: (username) => `${username} ユーザーを作成しました。`,
    createUserError: "ユーザーを作成できませんでした。",
    saveDealError: "案件を保存できませんでした。サーバーと入力内容を確認してください。",
    deleteDealError: "案件を削除できませんでした。",
    stages: {
      REGISTRATION: {
        name: "登録",
        description: "新規顧客と商談を受け付けます。",
      },
      ACCESS: {
        name: "アクセス",
        description: "関係者への接点とニーズを確認します。",
      },
      GO_NO_GO: {
        name: "Go / No Go",
        description: "条件確認と入札判断を行います。",
      },
      AWARD: {
        name: "受注",
        description: "優先交渉または最終交渉を進めます。",
      },
      CLOSED: {
        name: "完了",
        description: "受注、失注、または正式に完了した状態です。",
      },
    },
    priorities: {
      HIGH: "高",
      MEDIUM: "中",
      LOW: "低",
    },
  },
};

let deals = [];
const languageStorageKey = "crmLanguageV2";
let currentLanguage = localStorage.getItem(languageStorageKey) || "en";
if (!translations[currentLanguage]) {
  currentLanguage = "en";
}

let activeFilters = {
  search: "",
  stage: "all",
  priority: "all",
};

const pipeline = document.querySelector("#pipeline");
const languageSelect = document.querySelector("#languageSelect");
const stageFilter = document.querySelector("#stageFilter");
const priorityFilter = document.querySelector("#priorityFilter");
const searchInput = document.querySelector("#searchInput");
const dealDialog = document.querySelector("#dealDialog");
const dealForm = document.querySelector("#dealForm");
const deleteButton = document.querySelector("#deleteButton");
const dialogTitle = document.querySelector("#dialogTitle");
const stageSelect = document.querySelector("#stage");
const currentUsername = document.querySelector("#currentUsername");
const currentPassword = document.querySelector("#currentPassword");
const createUserButton = document.querySelector("#createUserButton");

function t(key) {
  return key.split(".").reduce((value, part) => value?.[part], translations[currentLanguage]) || key;
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(readErrorMessage(message) || `Request failed with ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

function readErrorMessage(message) {
  if (!message) return "";

  try {
    const parsed = JSON.parse(message);
    return parsed.message || parsed.error || message;
  } catch {
    return message;
  }
}

async function loadDeals() {
  const params = new URLSearchParams();
  if (activeFilters.search.trim()) params.set("search", activeFilters.search.trim());
  if (activeFilters.stage !== "all") params.set("stage", activeFilters.stage);
  if (activeFilters.priority !== "all") params.set("priority", activeFilters.priority);

  deals = await request(`/api/deals${params.toString() ? `?${params}` : ""}`);
  render();
}

function setupOptions() {
  stages.forEach((stage) => {
    const stageFilterOption = new Option(stage.name, stage.name);
    stageFilterOption.dataset.stageCode = stage.code;
    stageFilter.append(stageFilterOption);

    const stageFormOption = new Option(stage.name, stage.code);
    stageFormOption.dataset.stageCode = stage.code;
    stageSelect.append(stageFormOption);
  });
}

function render() {
  applyLanguage();
  renderMetrics();
  pipeline.innerHTML = "";

  stages.forEach((stage) => {
    const column = document.querySelector("#stageTemplate").content.cloneNode(true);
    const stageDeals = deals.filter((deal) => deal.stageCode === stage.code);
    const columnElement = column.querySelector(".stage-column");
    const list = column.querySelector(".deal-list");

    column.querySelector("h2").textContent = t(`stages.${stage.code}.name`);
    column.querySelector("p").textContent = t(`stages.${stage.code}.description`);
    column.querySelector(".stage-count").textContent = stageDeals.length;

    if (stageDeals.length === 0) {
      const empty = document.createElement("div");
      empty.className = "empty";
      empty.textContent = t("noDeals");
      list.append(empty);
    } else {
      stageDeals.forEach((deal) => list.append(renderDealCard(deal)));
    }

    columnElement.dataset.stage = stage.name;
    pipeline.append(column);
  });
}

function renderMetrics() {
  const total = deals.reduce((sum, deal) => sum + Number(deal.value), 0);
  const weighted = deals.reduce(
    (sum, deal) => sum + (Number(deal.value) * Number(deal.probability)) / 100,
    0,
  );
  const accounts = new Set(deals.map((deal) => deal.company)).size;

  document.querySelector("#totalDeals").textContent = deals.length;
  document.querySelector("#totalValue").textContent = formatCurrency(total);
  document.querySelector("#weightedValue").textContent = formatCurrency(weighted);
  document.querySelector("#activeAccounts").textContent = accounts;
}

function renderDealCard(deal) {
  const card = document.querySelector("#dealTemplate").content.cloneNode(true);
  const cardElement = card.querySelector(".deal-card");
  const priority = card.querySelector(".priority");
  const currentIndex = stages.findIndex((stage) => stage.code === deal.stageCode);
  const closeDate = new Date(`${deal.closeDate}T00:00:00`);

  card.querySelector(".deal-title").textContent = deal.company;
  card.querySelector(".deal-meta").textContent =
    `${deal.contact} - ${deal.owner} - ${formatDate(closeDate)}`;
  card.querySelector(".deal-action").textContent = deal.nextAction;
  priority.textContent = t(`priorities.${deal.priorityCode}`);
  priority.classList.add(deal.priority);
  card.querySelector(".value").textContent = formatCurrency(deal.value);

  card.querySelector(".deal-main").addEventListener("click", () => openDialog(deal.id));

  card.querySelectorAll(".move-button").forEach((button) => {
    const direction = button.dataset.direction;
    button.disabled =
      (direction === "back" && currentIndex === 0) ||
      (direction === "next" && currentIndex === stages.length - 1);
    button.textContent = direction === "back" ? t("prev") : t("next");
    button.addEventListener("click", async () => {
      await moveDeal(deal.id, direction);
    });
  });

  cardElement.dataset.id = deal.id;
  return card;
}

async function moveDeal(id, direction) {
  await request(`/api/deals/${id}/move`, {
    method: "PATCH",
    body: JSON.stringify({
      direction,
      username: currentUsername.value.trim(),
      password: currentPassword.value,
    }),
  });
  await loadDeals();
}

async function createUser() {
  const username = currentUsername.value.trim();
  const password = currentPassword.value;

  if (!username || !password) {
    alert(t("createUserRequired"));
    return;
  }

  await request("/api/users", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });

  alert(t("userCreated")(username));
}

function openDialog(id) {
  const deal = deals.find((item) => item.id === id);
  const isEditing = Boolean(deal);

  dialogTitle.textContent = isEditing ? t("editDeal") : t("newDeal");
  deleteButton.hidden = !isEditing;

  dealForm.reset();
  document.querySelector("#dealId").value = deal?.id || "";
  document.querySelector("#company").value = deal?.company || "";
  document.querySelector("#contact").value = deal?.contact || "";
  document.querySelector("#owner").value = deal?.owner || "";
  document.querySelector("#stage").value = deal?.stageCode || "REGISTRATION";
  document.querySelector("#value").value = deal?.value || 0;
  document.querySelector("#probability").value = deal?.probability || 10;
  document.querySelector("#priority").value = deal?.priorityCode || "MEDIUM";
  document.querySelector("#closeDate").value = deal?.closeDate || todayIso();
  document.querySelector("#opportunityLocation").value = deal?.opportunityLocation || "";
  document.querySelector("#expectedItems").value = deal?.expectedItems || "";
  document.querySelector("#nextAction").value = deal?.nextAction || "";

  if (!isEditing && currentUsername.value.trim()) {
    document.querySelector("#owner").value = currentUsername.value.trim();
  }

  dealDialog.showModal();
}

function closeDialog() {
  dealDialog.close();
}

async function handleSubmit(event) {
  event.preventDefault();
  const id = document.querySelector("#dealId").value;
  const payload = {
    company: document.querySelector("#company").value.trim(),
    contact: document.querySelector("#contact").value.trim(),
    owner: document.querySelector("#owner").value.trim(),
    stage: document.querySelector("#stage").value,
    value: Number(document.querySelector("#value").value),
    probability: Number(document.querySelector("#probability").value),
    priority: document.querySelector("#priority").value,
    closeDate: document.querySelector("#closeDate").value,
    opportunityLocation: document.querySelector("#opportunityLocation").value.trim(),
    expectedItems: document.querySelector("#expectedItems").value.trim(),
    nextAction: document.querySelector("#nextAction").value.trim(),
  };

  await request(id ? `/api/deals/${id}` : "/api/deals", {
    method: id ? "PUT" : "POST",
    body: JSON.stringify(payload),
  });

  closeDialog();
  await loadDeals();
}

async function deleteActiveDeal() {
  const id = document.querySelector("#dealId").value;
  if (!id) return;

  await request(`/api/deals/${id}`, { method: "DELETE" });
  closeDialog();
  await loadDeals();
}

function formatDate(date) {
  return new Intl.DateTimeFormat(getLocale(), {
    month: "short",
    day: "numeric",
    year: "numeric",
  }).format(date);
}

function formatCurrency(value) {
  return new Intl.NumberFormat(getLocale(), {
    style: "currency",
    currency: "USD",
    maximumFractionDigits: 0,
  }).format(value);
}

function getLocale() {
  if (currentLanguage === "ja") return "ja-JP";
  if (currentLanguage === "ko") return "ko-KR";
  return "en-US";
}

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

function showLoadError(error) {
  pipeline.innerHTML = "";
  const message = document.createElement("div");
  message.className = "empty";
  message.textContent = t("loadError");
  pipeline.append(message);
  console.error(error);
}

function applyLanguage() {
  document.documentElement.lang = currentLanguage;
  document.title = t("appTitle");
  languageSelect.value = currentLanguage;

  document.querySelectorAll("[data-i18n]").forEach((element) => {
    element.textContent = t(element.dataset.i18n);
  });

  document.querySelectorAll("[data-i18n-placeholder]").forEach((element) => {
    element.placeholder = t(element.dataset.i18nPlaceholder);
  });

  document.querySelectorAll("[data-i18n-aria-label]").forEach((element) => {
    element.setAttribute("aria-label", t(element.dataset.i18nAriaLabel));
  });

  document.querySelectorAll("[data-stage-code]").forEach((option) => {
    option.textContent = t(`stages.${option.dataset.stageCode}.name`);
  });

  document.querySelectorAll("[data-priority-option]").forEach((option) => {
    option.textContent = t(`priorities.${option.dataset.priorityOption}`);
  });

  if (dealDialog.open) {
    const isEditing = Boolean(document.querySelector("#dealId").value);
    dialogTitle.textContent = isEditing ? t("editDeal") : t("newDeal");
  }
}

setupOptions();
applyLanguage();
loadDeals().catch(showLoadError);

languageSelect.addEventListener("change", (event) => {
  currentLanguage = event.target.value;
  localStorage.setItem(languageStorageKey, currentLanguage);
  render();
});
document.querySelector("#newDealButton").addEventListener("click", () => openDialog());
document.querySelector("#closeDialogButton").addEventListener("click", closeDialog);
document.querySelector("#cancelButton").addEventListener("click", closeDialog);
createUserButton.addEventListener("click", () => {
  createUser().catch((error) => {
    console.error(error);
    alert(error.message || t("createUserError"));
  });
});
dealForm.addEventListener("submit", (event) => {
  handleSubmit(event).catch((error) => {
    console.error(error);
    alert(error.message || t("saveDealError"));
  });
});
deleteButton.addEventListener("click", () => {
  deleteActiveDeal().catch((error) => {
    console.error(error);
    alert(error.message || t("deleteDealError"));
  });
});
searchInput.addEventListener("input", (event) => {
  activeFilters.search = event.target.value;
  loadDeals().catch(showLoadError);
});
stageFilter.addEventListener("change", (event) => {
  activeFilters.stage = event.target.value;
  loadDeals().catch(showLoadError);
});
priorityFilter.addEventListener("change", (event) => {
  activeFilters.priority = event.target.value;
  loadDeals().catch(showLoadError);
});
