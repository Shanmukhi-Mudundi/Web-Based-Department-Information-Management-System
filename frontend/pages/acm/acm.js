(() => {
  const apiBase =
    document.querySelector('meta[name="api-base"]')?.getAttribute("content")?.trim() ||
    "http://localhost:8081";

  const infoSection = document.getElementById("acm-info");
  const highlightsContainer = document.getElementById("acm-highlights");
  const benefitsContainer = document.getElementById("acm-benefits");
  const eventsContainer = document.getElementById("acm-events");
  const reportModal = document.getElementById("reportModal");
  const reportModalTitle = document.getElementById("reportModalTitle");
  const reportModalFrame = document.getElementById("reportModalFrame");
  const reportModalClose = document.getElementById("reportModalClose");

  function clearContainer(container, emptyMessage) {
    if (!container) return;
    container.innerHTML = "";
    if (emptyMessage) {
      const empty = document.createElement("p");
      empty.className = "empty-state";
      empty.textContent = emptyMessage;
      container.appendChild(empty);
    }
  }

  function setInfo(info) {
    if (!infoSection) return;
    const title =
      info?.organization ||
      info?.title ||
      info?.name ||
      "About ACM";

    const description =
      info?.aboutACM ||
      info?.description ||
      info?.about ||
      "ACM brings together curious minds to learn, build, and share.";

    const heading = infoSection.querySelector("h2");
    const paragraph = infoSection.querySelector("p");
    if (heading) heading.textContent = title;
    if (paragraph) paragraph.textContent = description;

    if (highlightsContainer) {
      highlightsContainer.innerHTML = "";
      const highlights = [
        info?.college,
        info?.aboutChapters,
        info?.aboutACMWStudentChapter,
        info?.chapter,
        info?.mission,
        info?.vision,
      ].filter(Boolean);
      if (!highlights.length) {
        const fallback = document.createElement("div");
        fallback.className = "info-pill";
        fallback.textContent = "Community-driven learning";
        highlightsContainer.appendChild(fallback);
        return;
      }
      highlights.forEach((item) => {
        const pill = document.createElement("div");
        pill.className = "info-pill";
        pill.textContent = item;
        highlightsContainer.appendChild(pill);
      });
    }
  }

  function createBenefitCard(benefit) {
    const card = document.createElement("div");
    card.className = "list-card";

    const body = document.createElement("p");
    body.textContent =
      benefit?.benefit ||
      benefit?.description ||
      benefit?.details ||
      benefit?.summary ||
      "";

    const titleText = benefit?.title || benefit?.name || "";
    if (titleText) {
      const title = document.createElement("h3");
      title.textContent = titleText;
      card.appendChild(title);
    }

    card.appendChild(body);
    return card;
  }

  function createEventCard(event) {
    const card = document.createElement("div");
    card.className = "list-card";

    const title = document.createElement("h3");
    title.textContent = event?.eventName || event?.name || event?.title || "Event";

    const body = document.createElement("p");
    body.textContent = event?.description || event?.details || event?.summary || "";

    const meta = document.createElement("div");
    meta.className = "meta-row";

    if (event?.date) {
      const badge = document.createElement("span");
      badge.className = "badge";
      badge.textContent = event.date;
      meta.appendChild(badge);
    }

    if (event?.type) {
      const type = document.createElement("span");
      type.textContent = event.type;
      meta.appendChild(type);
    }

    if (event?.participants !== undefined && event?.participants !== null) {
      const participants = document.createElement("span");
      participants.textContent = `Participants: ${event.participants}`;
      meta.appendChild(participants);
    }

    if (event?.location) {
      const place = document.createElement("span");
      place.textContent = event.location;
      meta.appendChild(place);
    }

    const reportPreviewUrl = getReportPreviewUrl(event);
    if (reportPreviewUrl) {
      const reportBadge = document.createElement("span");
      reportBadge.className = "badge badge--report";
      reportBadge.textContent = event?.reportTitle || "View report";
      meta.appendChild(reportBadge);

      card.classList.add("list-card--interactive");
      card.tabIndex = 0;
      card.setAttribute("role", "button");
      card.setAttribute("aria-haspopup", "dialog");
      card.addEventListener("click", () => openReportModal(event));
      card.addEventListener("keydown", (keyboardEvent) => {
        if (keyboardEvent.key === "Enter" || keyboardEvent.key === " ") {
          keyboardEvent.preventDefault();
          openReportModal(event);
        }
      });
    }

    card.append(title, body, meta);
    return card;
  }

  function getReportPreviewUrl(event) {
    if (!event) return null;

    const previewUrl = typeof event.reportPreviewUrl === "string" ? event.reportPreviewUrl.trim() : "";
    if (previewUrl) return previewUrl;

    const fileId = typeof event.reportFileId === "string" ? event.reportFileId.trim() : "";
    if (fileId) return `https://docs.google.com/document/d/${fileId}/preview`;

    return null;
  }

  function parseEventTimestamp(value) {
    if (typeof value !== "string") return null;
    const trimmed = value.trim();
    if (!trimmed) return null;

    const parsed = Date.parse(trimmed);
    if (!Number.isNaN(parsed)) return parsed;

    const dayFirstMatch = trimmed.match(/^(\d{1,2})[/-](\d{1,2})[/-](\d{4})$/);
    if (dayFirstMatch) {
      const [, day, month, year] = dayFirstMatch;
      return new Date(Number(year), Number(month) - 1, Number(day)).getTime();
    }

    return null;
  }

  function sortEventsRecentFirst(events) {
    return [...events].sort((left, right) => {
      const leftDate = parseEventTimestamp(left?.date);
      const rightDate = parseEventTimestamp(right?.date);

      if (leftDate === null && rightDate !== null) return 1;
      if (leftDate !== null && rightDate === null) return -1;
      if (leftDate !== null && rightDate !== null) {
        const dateDifference = rightDate - leftDate;
        if (dateDifference !== 0) return dateDifference;
      }

      const leftName = (left?.eventName || left?.name || left?.title || "").toLowerCase();
      const rightName = (right?.eventName || right?.name || right?.title || "").toLowerCase();
      return leftName.localeCompare(rightName);
    });
  }

  function openReportModal(event) {
    const previewUrl = getReportPreviewUrl(event);
    if (!previewUrl || !reportModal || !reportModalFrame) return;

    if (reportModalTitle) {
      reportModalTitle.textContent = event?.reportTitle || event?.eventName || "Report Preview";
    }

    reportModalFrame.src = previewUrl;
    reportModal.hidden = false;
    document.body.classList.add("acm-modal-open");
  }

  function closeReportModal() {
    if (!reportModal || !reportModalFrame) return;
    reportModal.hidden = true;
    reportModalFrame.src = "about:blank";
    document.body.classList.remove("acm-modal-open");
  }

  reportModal?.addEventListener("click", (event) => {
    const closeTrigger = event.target.closest("[data-close-report-modal]");
    if (closeTrigger) closeReportModal();
  });

  reportModalClose?.addEventListener("click", closeReportModal);

  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && reportModal && !reportModal.hidden) {
      closeReportModal();
    }
  });

  async function loadAcm() {
    try {
      const response = await fetch(`${apiBase.replace(/\/$/, "")}/api/acm`, {
        headers: { Accept: "application/json" },
      });
      if (!response.ok) return;
      const data = await response.json();

      setInfo(data?.info || {});

      if (benefitsContainer) {
        const benefits = Array.isArray(data?.benefits) ? data.benefits : [];
        if (!benefits.length) {
          clearContainer(benefitsContainer, "No benefits listed yet.");
        } else {
          benefitsContainer.innerHTML = "";
          benefits.forEach((benefit) => benefitsContainer.appendChild(createBenefitCard(benefit)));
        }
      }

      if (eventsContainer) {
        const events = Array.isArray(data?.events) ? sortEventsRecentFirst(data.events) : [];
        if (!events.length) {
          clearContainer(eventsContainer, "No upcoming events available.");
        } else {
          eventsContainer.innerHTML = "";
          events.forEach((event) => eventsContainer.appendChild(createEventCard(event)));
        }
      }
    } catch (err) {
      console.error("Failed to load ACM data:", err);
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", loadAcm);
  } else {
    loadAcm();
  }
})();
