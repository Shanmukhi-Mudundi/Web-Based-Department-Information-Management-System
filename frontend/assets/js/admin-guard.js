import { auth } from "./firebase-init.js";
import { onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/12.11.0/firebase-auth.js";
import { canAccessAdminScope, dashboardPath, isAllowedAdminEmail, loginPath } from "./admin-auth.js";

function getAdminScope() {
  return document.body?.dataset?.adminScope || "admin";
}

function attachLogout() {
  const btn = document.getElementById("logoutBtn");
  if (!btn) return;
  btn.addEventListener("click", async (event) => {
    event.preventDefault();
    await signOut(auth);
    window.location.href = loginPath;
  });
}

onAuthStateChanged(auth, async (user) => {
  const scope = getAdminScope();

  if (!user) {
    window.location.href = loginPath;
    return;
  }

  if (!isAllowedAdminEmail(user.email)) {
    await signOut(auth);
    window.location.href = loginPath;
    return;
  }

  if (!canAccessAdminScope(user.email, scope)) {
    window.location.href = dashboardPath;
    return;
  }

  attachLogout();
});
