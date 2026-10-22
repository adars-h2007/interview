# Recall — Interview Preparation Tracker

A full-stack interview preparation app that uses **spaced repetition** to schedule your practice questions. It uses the **SM-2 algorithm**, so questions you find hard come back sooner and questions you know well come back later.

## Features

- **Dashboard** with stats on your progress
- **Question library** to add, edit, browse and delete questions
- **SM-2 spaced repetition** that schedules each question's next review automatically
- **Due questions** view showing what to review today
- **Progress tracking and mastery scoring** for each question
- **Categories:** DSA, System Design, Behavioral, Database, Networking, Cloud, Other
- **Demo data:** 6 sample questions across categories, loaded on first run
- Purple and violet theme with gradient accents, responsive and mobile-first

## Tech used

| Layer | Technology |
|---|---|
| Frontend | React 18, Vite 5, CSS, JavaScript |
| Styling | Custom CSS, Google Fonts (Outfit, Space Mono) |
| Linting | ESLint (flat config, zero warnings) |
| Backend | Java 17, Spring Boot 3.2.5 |
| Data access | Hibernate / JPA |
| Database | MySQL (schema created automatically) |

## How the spaced repetition works

Each question stores the SM-2 fields used to schedule its next review:

| Field | Meaning |
|---|---|
| `easeFactor` | How easy the question is for you; higher means longer gaps |
| `interval` | Days until the next review |
| `repetitions` | Number of successful reviews in a row |
| `nextReviewAt` | When the question is next due |

After each review you rate how well you recalled the answer, and the app updates these fields to set the next review date.

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET`, `POST`, `PUT`, `DELETE` | `/api/questions` | Create, read, update and delete questions |
| `GET` | `/api/questions/due` | Questions due for review |
| `POST` | `/api/questions/{id}/review` | Submit a review and reschedule the question |
| `GET` | `/api/questions/stats/all` | Dashboard statistics |

<!-- Check the HTTP methods above against your controller and adjust if they differ. -->

## Project structure

```
interview/
└── interview-tracker/
    ├── frontend/      # React + Vite app
    └── backend/       # Spring Boot API (includes SeedData.java for demo questions)
```

<!-- Replace with your real folder names if they differ. -->

## Getting started

### Prerequisites

- [Node.js](https://nodejs.org/) (LTS)
- Java 17 or newer
- MySQL 8 or newer

### 1. Clone the repository

```bash
git clone https://github.com/adars-h2007/interview.git
cd interview/interview-tracker
```

### 2. Set up the database

Create an empty database. The tables are created automatically when the backend starts.

```sql
CREATE DATABASE interview_tracker;
```

Then set your MySQL username and password in the backend's `application.properties`.

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`, and the 6 demo questions are added on first start.

### 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173` in your browser.

## Frontend scripts

| Command | What it does |
|---|---|
| `npm run dev` | Start the development server |
| `npm run build` | Create a production build |
| `npm run preview` | Preview the production build locally |
| `npm run lint` | Check the code with ESLint (zero warnings allowed) |

## Roadmap

- [ ] User accounts so each person has their own library
- [ ] Import and export of questions
- [ ] Review reminders

## Author

**Adarsh Patil**

- GitHub: [@adars-h2007](https://github.com/adars-h2007)
- LinkedIn: [Adarsh Patil](https://www.linkedin.com/in/adarsh-patil-587a65319/)
- Email: adarshpatil6650@gmail.com
