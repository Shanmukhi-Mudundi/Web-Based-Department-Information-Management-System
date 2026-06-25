import { initializeApp } from "https://www.gstatic.com/firebasejs/12.11.0/firebase-app.js";
import { getAuth, GoogleAuthProvider } from "https://www.gstatic.com/firebasejs/12.11.0/firebase-auth.js";

const firebaseConfig = {
  apiKey: "AIzaSyB-J_-IsR82z8naV-VuWWvnSDSxGbiqpPc",
  authDomain: "department-management-sys.firebaseapp.com",
  projectId: "department-management-sys",
  storageBucket: "department-management-sys.firebasestorage.app",
  messagingSenderId: "773433508483",
  appId: "1:773433508483:web:7e3d98cb885c330bcd51b6",
  measurementId: "G-V26F9NRDB2"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const googleProvider = new GoogleAuthProvider();
googleProvider.setCustomParameters({
  prompt: "select_account"
});

export { app, auth, googleProvider };
