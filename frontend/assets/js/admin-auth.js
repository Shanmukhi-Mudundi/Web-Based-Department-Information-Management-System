// Optional: set a domain like "college.edu" or list specific admin emails.
const ALLOWED_DOMAIN = "";
const ALLOWED_EMAILS = [];
const COMPLAINTS_ALLOWED_EMAILS = [
  "asha.v@bvrithyderabad.edu.in",
  "hod.aiml@bvrithyderabad.edu.in"
];
const loginPath = "/pages/login.html";
const dashboardPath = "/pages/admin/dashboard.html";

function normalizeEmail(email) {
  return (email || "").trim().toLowerCase();
}

function isAllowedAdminEmail(email) {
  const normalizedEmail = normalizeEmail(email);
  if (!normalizedEmail) return false;

  const hasEmailAllowlist = ALLOWED_EMAILS.length > 0;
  const hasDomainRule = !!ALLOWED_DOMAIN;

  if (!hasEmailAllowlist && !hasDomainRule) {
    return true;
  }

  if (ALLOWED_EMAILS.map(normalizeEmail).includes(normalizedEmail)) {
    return true;
  }

  if (hasDomainRule && normalizedEmail.endsWith("@" + ALLOWED_DOMAIN.toLowerCase())) {
    return true;
  }

  return false;
}

function isAllowedComplaintViewer(email) {
  const normalizedEmail = normalizeEmail(email);
  if (!normalizedEmail) return false;
  return COMPLAINTS_ALLOWED_EMAILS.map(normalizeEmail).includes(normalizedEmail);
}

function canAccessAdminScope(email, scope = "admin") {
  if (!isAllowedAdminEmail(email)) {
    return false;
  }

  if (scope === "complaints") {
    return isAllowedComplaintViewer(email);
  }

  return true;
}

export {
  ALLOWED_DOMAIN,
  ALLOWED_EMAILS,
  COMPLAINTS_ALLOWED_EMAILS,
  canAccessAdminScope,
  dashboardPath,
  isAllowedAdminEmail,
  isAllowedComplaintViewer,
  loginPath
};
