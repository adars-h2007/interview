# 🧠 Recall: Interview Preparation Tracker with Spaced Repetition

A full-stack application to help you ace technical interviews through intelligent spaced repetition learning.

## Features

✨ **Spaced Repetition Algorithm** - Uses the SM-2 algorithm to optimize review timing  
📚 **Question Library** - Organize questions by category (DSA, System Design, Behavioral, etc.)  
📊 **Progress Tracking** - Dashboard with mastery scores, stats, and learning analytics  
🎯 **Smart Scheduling** - Questions automatically scheduled based on your performance  
💾 **Persistent Storage** - MySQL database for reliable data management  
🎨 **Beautiful UI** - Modern gradient design with smooth animations  

## Tech Stack

**Frontend:**
- React 18 with Vite
- Plain CSS with modern features (grid, flex, backdrop-filter)
- Responsive design (mobile-first)

**Backend:**
- Java 17 with Spring Boot 3.2.5
- Spring Data JPA with Hibernate
- MySQL 8 database

## Project Structure

```
interview-tracker/
├── package.json          # Root npm commands for the frontend
├── frontend/
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── App.jsx
│   │   ├── api.js         # API client
│   │   ├── constants.js   # Constants & utilities
│   │   └── index.css      # Global styles
│   ├── public/
│   │   └── favicon.svg
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   ├── eslint.config.js
│   ├── .gitignore
│   └── .nvmrc
└── backend/
    ├── src/main/java/com/interview/tracker/
    │   ├── model/           # Domain entities
    │   ├── repository/      # Data access
    │   ├── service/         # Business logic
    │   ├── controller/      # REST endpoints
    │   └── config/          # Configuration
    ├── src/main/resources/
    │   ├── application.properties
    │   └── database/
    │       └── schema.sql
    └── pom.xml
```

## Getting Started

### Prerequisites
- Node.js 20+
- Java 17+
- MySQL 8+

### Root npm Commands

From the repository root, install frontend dependencies and run the common frontend commands:

```bash
npm run setup
npm run dev
npm run build
npm run preview
npm run lint
npm run lint:fix
```

### Database Setup

The backend stores accounts, interview questions, spaced repetition state, and each review rating in MySQL. Start a local MySQL 8 database with Docker:

Each account receives six starter interview questions on registration. Accounts created before this feature receive them the next time they sign in, once.

```bash
docker compose up -d database
```

The database and tables are created on the first start and stored in the `interview-tracker-data` Docker volume. The default local connection is `localhost:3306` with database/user/password `interview_tracker`. To reset all local data, run `docker compose down -v`.

To add a saved MySQL Workbench connection on Windows, run `powershell -ExecutionPolicy Bypass -File .\scripts\Add-WorkbenchConnection.ps1`. Start the database first with `docker compose up -d database`; then open the **Interview Tracker (Local)** connection in Workbench. It uses `localhost:3306`, schema `interview_tracker`, and user `interview_tracker`. Enter the local development password `interview_tracker` when Workbench prompts. The script leaves other saved Workbench connections unchanged and does not save a password.

If you are using an already installed MySQL server instead of the Docker database, connect in Workbench with a MySQL administrator account and run `backend/src/main/resources/database/local-workbench-user.sql`. This creates/resets the local `interview_tracker` account used by the app and the saved Workbench connection. The SQL uses a development-only password; choose a different password and set `DB_PASSWORD` if needed.

For an existing MySQL server, execute `backend/src/main/resources/database/schema.sql` once, then set `DB_URL`, `DB_USER`, and `DB_PASSWORD` in the environment before starting the backend. Do not use the example credentials for a publicly accessible database.

### Backend Setup

1. **Run Backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   # Runs on http://localhost:8080
   ```

### Frontend Setup

1. **Install Dependencies:**
   ```bash
   cd frontend
   npm install
   ```

2. **Run Development Server:**
   ```bash
   npm run dev
   # Opens http://localhost:5173
   ```

3. **Build for Production:**
   ```bash
   npm run build
   npm run preview
   ```

4. **Lint Code:**
   ```bash
   npm run lint
   npm run lint:fix
   ```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/questions` | List all questions |
| GET | `/api/questions/due` | Get questions due for review |
| GET | `/api/questions/{id}` | Get question detail |
| POST | `/api/questions` | Create new question |
| PUT | `/api/questions/{id}` | Update question |
| POST | `/api/questions/{id}/review` | Submit review (quality: 0-5) |
| DELETE | `/api/questions/{id}` | Delete question |
| GET | `/api/questions/stats/all` | Get statistics |

## Spaced Repetition Algorithm

The app uses the **SM-2 algorithm**:

1. **Quality Rating** (0-5): Rate how well you answered
2. **Ease Factor**: Adjusted based on performance (min 1.3, start 2.5)
3. **Interval**: Days until next review
4. **Repetitions**: Number of times reviewed

**Logic:**
- Quality ≥ 3: Increase interval by ease factor
- Quality < 3: Reset to 1-day interval
- First review: 1 day
- Second review: 3 days
- Third+ review: interval × ease factor

## Dashboard Sections

📊 **Stats Cards** - Total questions, due reviews, mastered, average mastery  
🔴 **Due for Review** - Questions ready to practice (click to review)  
📚 **Questions View** - Browse all questions, filter by category, search, adjust difficulty  

## Keyboard Shortcuts

- `Ctrl/Cmd + K` - Focus search (when implemented)

## Configuration

### Environment Variables

**Frontend** (`frontend/.env`):
```
VITE_API_URL=http://localhost:8080/api
```

**Backend** (`backend/application.properties`):
```
DB_USER=root
DB_PASSWORD=root
FRONTEND_ORIGIN=http://localhost:5173
```

## Deployment

**Frontend (Vercel/Netlify):**
```bash
npm run build
# Deploy dist/ folder
```

**Backend (Heroku/Railway):**
```bash
mvn clean package
# Deploy with application.properties configured
```

## Production Considerations

⚠️ **Before going live:**
- Add user authentication
- Implement per-user data isolation
- Add rate limiting
- Set up SSL/HTTPS
- Configure CORS properly for your domain
- Add request logging & monitoring
- Set up database backups
- Add input validation & sanitization

## Future Enhancements

- 🔐 User authentication & accounts
- 📱 Mobile app (React Native)
- 🔊 Audio explanations
- 🎬 Video answer support
- 📤 Import/export questions
- 🤝 Collaboration & sharing
- 🎓 Interview tracks (FAANG, startups, etc.)
- 📊 Advanced analytics & charts
- 🔔 Email/SMS reminders
- ⌨️ Keyboard shortcuts

## License

MIT - Use freely for learning and personal projects

## Contributing

Pull requests welcome! Please:
1. Fork the repo
2. Create a feature branch
3. Commit your changes
4. Push and create a PR

---

**Made with 💜 for interview prep success!**
