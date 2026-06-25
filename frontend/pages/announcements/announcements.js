(() => {
  const container = document.querySelector(".announcement-container");
  if (!container) return;

  const apiBase =
    document.querySelector('meta[name="api-base"]')?.getAttribute("content")?.trim() ||
    "http://localhost:8081";

  function formatPostedAt(isoString) {
    if (!isoString) return "";
    const date = new Date(isoString);
    if (Number.isNaN(date.getTime())) return "";
    return date.toLocaleDateString(undefined, { day: "2-digit", month: "short", year: "numeric" });
  }

  function createCard(item) {
    const card = document.createElement("div");
    card.className = `announcement${item.important ? " important" : ""}`;

    const tag = document.createElement("span");
    tag.className = `tag${item.important ? "" : " normal"}`;
    tag.textContent = item.tag || (item.important ? "IMPORTANT" : "NOTICE");

    const title = document.createElement("h3");
    title.textContent = item.title || "";

    const body = document.createElement("p");
    body.textContent = item.body || "";

    const date = document.createElement("span");
    date.className = "date";
    const postedAt = formatPostedAt(item.postedAt);
    date.textContent = postedAt ? `Posted on: ${postedAt}` : "";

    card.append(tag, title, body, date);
    return card;
  }

  function parseDateValue(value) {
    if (!value) return null;
    if (typeof value === "number") return value;
    let raw = String(value).trim();
    if (!raw) return null;
    raw = raw.replace(/^posted on:\s*/i, "");
    const parsed = Date.parse(raw);
    if (!Number.isNaN(parsed)) return parsed;

    const monthMap = {
      jan: 0, january: 0,
      feb: 1, february: 1,
      mar: 2, march: 2,
      apr: 3, april: 3,
      may: 4,
      jun: 5, june: 5,
      jul: 6, july: 6,
      aug: 7, august: 7,
      sep: 8, sept: 8, september: 8,
      oct: 9, october: 9,
      nov: 10, november: 10,
      dec: 11, december: 11
    };

    const named = raw.match(/^(\d{1,2})\s*([A-Za-z]{3,})\s*(\d{4})$/);
    if (named) {
      const day = Number(named[1]);
      const month = monthMap[named[2].toLowerCase()];
      const year = Number(named[3]);
      if (!Number.isNaN(day) && month !== undefined && !Number.isNaN(year)) {
        return new Date(year, month, day).getTime();
      }
    }

    const dmy = raw.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})$/);
    if (dmy) {
      const day = Number(dmy[1]);
      const month = Number(dmy[2]) - 1;
      const year = Number(dmy[3]);
      if (!Number.isNaN(day) && !Number.isNaN(month) && !Number.isNaN(year)) {
        return new Date(year, month, day).getTime();
      }
    }

    return null;
  }

  function sortByRecent(items) {
    return items
      .map((item, index) => ({
        item,
        index,
        time: parseDateValue(item?.postedAt || item?.date || item?.createdAt)
      }))
      .sort((a, b) => {
        if (a.time === null && b.time === null) return b.index - a.index;
        if (a.time === null) return 1;
        if (b.time === null) return -1;
        return b.time - a.time;
      })
      .map(entry => entry.item);
  }

  async function load() {
    try {
      const response = await fetch(`${apiBase.replace(/\\/$/, "")}/api/announcements`, {
        headers: { Accept: "application/json" },
      });
      if (!response.ok) return;
      const data = await response.json();
      const items = Array.isArray(data?.items) ? data.items : [];
      if (!items.length) return;

      container.innerHTML = "";
      for (const item of sortByRecent(items)) container.append(createCard(item));
    } catch {
      // keep static HTML fallback if API is unreachable
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", load);
  } else {
    load();
  }
})();
