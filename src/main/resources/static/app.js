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

const currency = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
  maximumFractionDigits: 0,
});

let deals = [];
let activeFilters = {
  search: "",
  stage: "all",
  priority: "all",
};

const pipeline = document.querySelector("#pipeline");
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
    stageFilter.append(new Option(stage.name, stage.name));
    stageSelect.append(new Option(stage.name, stage.code));
  });
}

function render() {
  renderMetrics();
  pipeline.innerHTML = "";

  stages.forEach((stage) => {
    const column = document.querySelector("#stageTemplate").content.cloneNode(true);
    const stageDeals = deals.filter((deal) => deal.stageCode === stage.code);
    const columnElement = column.querySelector(".stage-column");
    const list = column.querySelector(".deal-list");

    column.querySelector("h2").textContent = stage.name;
    column.querySelector("p").textContent = stage.description;
    column.querySelector(".stage-count").textContent = stageDeals.length;

    if (stageDeals.length === 0) {
      const empty = document.createElement("div");
      empty.className = "empty";
      empty.textContent = "No deals";
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
  document.querySelector("#totalValue").textContent = currency.format(total);
  document.querySelector("#weightedValue").textContent = currency.format(weighted);
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
  priority.textContent = deal.priority;
  priority.classList.add(deal.priority);
  card.querySelector(".value").textContent = currency.format(deal.value);

  card.querySelector(".deal-main").addEventListener("click", () => openDialog(deal.id));

  card.querySelectorAll(".move-button").forEach((button) => {
    const direction = button.dataset.direction;
    button.disabled =
      (direction === "back" && currentIndex === 0) ||
      (direction === "next" && currentIndex === stages.length - 1);
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
    alert("Enter a username and password before creating a user.");
    return;
  }

  await request("/api/users", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });

  alert(`User ${username} was created.`);
}

function openDialog(id) {
  const deal = deals.find((item) => item.id === id);
  const isEditing = Boolean(deal);

  dialogTitle.textContent = isEditing ? "Edit Deal" : "New Deal";
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
  return new Intl.DateTimeFormat("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  }).format(date);
}

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

function showLoadError(error) {
  pipeline.innerHTML = "";
  const message = document.createElement("div");
  message.className = "empty";
  message.textContent = "Could not load CRM data. Check that Spring Boot is running.";
  pipeline.append(message);
  console.error(error);
}

setupOptions();
loadDeals().catch(showLoadError);

document.querySelector("#newDealButton").addEventListener("click", () => openDialog());
document.querySelector("#closeDialogButton").addEventListener("click", closeDialog);
document.querySelector("#cancelButton").addEventListener("click", closeDialog);
createUserButton.addEventListener("click", () => {
  createUser().catch((error) => {
    console.error(error);
    alert(error.message || "Could not create user.");
  });
});
dealForm.addEventListener("submit", (event) => {
  handleSubmit(event).catch((error) => {
    console.error(error);
    alert(error.message || "Could not save deal. Check the server and input values.");
  });
});
deleteButton.addEventListener("click", () => {
  deleteActiveDeal().catch((error) => {
    console.error(error);
    alert(error.message || "Could not delete deal.");
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
