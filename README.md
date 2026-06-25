# Web-Based Department Information Management System

[![License](https://img.shields.io/badge/license-academic-lightgrey)]()
[![Java](https://img.shields.io/badge/Java-21%2B-blue)]()
[![MongoDB](https://img.shields.io/badge/Database-MongoDB-green)]()
[![Status](https://img.shields.io/badge/status-development-orange)]()

A full‑stack portal for the CSE (AI & ML) department at BVRIT Hyderabad — centralizes placements, timetables, announcements, achievements and more, with role-based access for Admin / CRC / Faculty / Student.

Why this repo
- Makes departmental information searchable and manageable from one place.
- Provides an Admin dashboard for non-technical staff to upload timetables, announcements and weekly reports.
- Demonstrates a Java + Spring Boot backend with a static frontend and Firebase Authentication.

Table of contents
- [Highlights](#highlights)
- [Screenshots / Demo](#screenshots--demo)
- [Tech stack](#tech-stack)
- [Quick start](#quick-start)
  - [Prerequisites](#prerequisites)
  - [Run backend](#run-backend)
  - [Run frontend](#run-frontend)
- [Configuration](#configuration)
- [Seed / sample data](#seed--sample-data)
- [API examples](#api-examples)
- [Auth & roles](#auth--roles)
- [Project structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

Highlights
- Admin Dashboard to manage announcements, achievements, timetables, complaints and weekly reports.
- Role-based access using Firebase Authentication.
- MongoDB + GridFS for storing timetable PDFs.
- REST API built with Spring Boot (Java 21+).

Screenshots / Demo
- (Optional) Add images to `docs/images/` and reference them here.
- Example:
  ![Home page screenshot](docs/images/screenshot-home.png)
- GIFs are great for short flows (create announcement, upload PDF).

Tech stack
- Frontend: HTML, CSS, JavaScript
- Backend: Java, Spring Boot
- Database: MongoDB (+ GridFS for files)
- Auth: Firebase Authentication
- Build: Maven

Quick start

Prerequisites
- Java 21+
- Maven
- MongoDB (local or Atlas)
- Node / Live Server extension (optional, for local frontend preview)
- Firebase project (Authentication enabled)

Run backend
1. Clone the repo and start from the backend folder:
   ```bash
   git clone https://github.com/Shanmukhi-Mudundi/Web-Based-Department-Information-Management-System.git
   cd Web-Based-Department-Information-Management-System/backend
   ```
2. Update configuration (see [Configuration](#configuration)).
3. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Backend default: http://localhost:8081

Run frontend
- Option A — Open static files:
  - Open `frontend/pages/index.html` in your browser.
- Option B — Serve via Live Server (recommended for relative requests):
  - Open the `frontend` folder in VS Code and start Live Server.

Configuration
- Edit `src/main/resources/application.properties` (backend) or use environment variables.
- Example application.properties:
  ```properties
  spring.data.mongodb.uri=mongodb://localhost:27017/departmentDB
  spring.data.mongodb.database=departmentDB
  server.port=8081
  app.admin-token=YOUR_SECRET_TOKEN
  ```
- Example .env.example (if you prefer env style):
  ```env
  MONGODB_URI=mongodb://localhost:27017/departmentDB
  MONGODB_DATABASE=departmentDB
  SERVER_PORT=8081
  APP_ADMIN_TOKEN=changeme
  FIREBASE_API_KEY=your_firebase_api_key
  FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
  ```

Seed / sample data
- To view the app quickly, pre-populate the database with a few announcements, placements, and a timetable.
- Add a `data/seed.json` and import with `mongoimport` (example):
  ```bash
  mongoimport --uri="mongodb://localhost:27017/departmentDB" --collection announcements --file data/announcements.json --jsonArray
  ```
- If you want, I can create small JSON seed files for announcements, placements and toppers.

API examples
- Get all announcements:
  ```bash
  curl http://localhost:8081/api/announcements
  ```
- Post an anonymous complaint:
  ```bash
  curl -X POST http://localhost:8081/api/complaints \
    -H "Content-Type: application/json" \
    -d '{"category":"infrastructure","message":"Projector not working in lab 2"}'
  ```
- Admin-protected endpoint (example header usage):
  ```bash
  curl -H "Authorization: Bearer <ADMIN_TOKEN>" http://localhost:8081/api/timetables
  ```

Auth & roles
- Authentication handled by Firebase Auth on the frontend.
- Roles: Admin, CRC Member, Faculty, Student.
- Admins get extra privileges via `app.admin-token` or role claims (document how you assign these in Firebase).
- Add a short how-to on creating test users in Firebase Console and setting role claims (I can write this if you want).

Project structure
```
├── frontend/
│   ├── pages/
│   ├── assets/
│   │   ├── css/
│   │   ├── js/
│   │   └── images/
│   └── components/
└── backend/
    └── src/main/java/com/department/dms/
        ├── controller/
        ├── service/
        ├── model/
        ├── repository/
        └── config/
```

Contributing
- If you accept contributions: please open issues first to discuss features/bugs.
- Suggested workflow:
  1. Fork repo
  2. Create branch: `git checkout -b feat/short-description`
  3. Commit & push
  4. Open a Pull Request against `main`
- Add a `CONTRIBUTING.md` for preferred code style and review rules.

License
- This project was developed as an academic mini-project for BVRIT Hyderabad (2025–2026).
- If you want a formal license (MIT/Apache/etc.) add a `LICENSE` file. I can add an MIT template if you prefer.

Contact
- Project lead: Mudundi Shanmukhi — 23WH1A6630
- Guide: Ms. Asha Vuyyuru, Assistant Professor, CSE (AI & ML)

Roadmap / Next steps (optional)
- Add unit & integration tests for backend.
- Add CI (GitHub Actions) and a build badge.
- Deploy backend (Render / Railway / Fly) and frontend (GitHub Pages / Netlify) and add a demo link.
