# URL Shortener

A full-stack URL shortening service built with Spring Boot and React.

🔗 **Live Demo:** https://url-shortener-sigma-lovat.vercel.app

## Features
- Shorten any URL instantly
- Custom expiry dates
- Click tracking and analytics
- Automatic cleanup of expired links
- Input validation and error handling

## Tech Stack
**Backend**
- Java 17 + Spring Boot 3.5.1
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Validation

**Frontend**
- React + Vite
- Tailwind CSS

**Deployment**
- Backend: Railway
- Frontend: Vercel
- Database: PostgreSQL on Railway

## Architecture

User → Vercel (React) → Railway (Spring Boot) → PostgreSQL

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/shorten | Shorten a URL |
| GET | /{shortCode} | Redirect to original URL |
| GET | /api/stats/{shortCode} | Get link statistics |
| DELETE | /api/urls/{shortCode} | Delete a link |

## Run Locally

**Backend**
```bash
cd urlshortener
./mvnw spring-boot:run
```

**Frontend**
```bash
cd frontend
npm install
npm run dev
```

## Design Decisions
- **Base62 encoding** generates 6-character codes giving 56 billion unique combinations
- **Collision detection** re-generates codes if a duplicate is found
- **Scheduled cleanup** runs nightly to delete expired links
- **Environment-based configuration** for easy deployment