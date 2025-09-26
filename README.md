### Student Grade Management Program

### Group Project Overview

Welcome to the Student Grade Management Program! This project was built by Omar, Ali, and I to learn Spring Boot and React while creating something impressive. Our goal was to design a robust web-based platform for managing student grades that showcases our skills in full-stack development.

This program not only demonstrates our technical expertise but also highlights our ability to collaborate effectively on complex projects. We're proud to present this as a testament to our teamwork and commitment to excellence.

### Quick Start

Backend

1. Clone the repo and navigate to the backend folder.
2. Run the backend with:
   mvn spring-boot:run
3. Access the API at http://localhost:2800 and Swagger UI at http://localhost:2800/swagger-ui/index.html.

Frontend

1. Navigate to the frontend folder.
2. Build the frontend:
   npm ci
   (If it fails, run npm install instead.)
3. Start the frontend:
   npm run dev
4. Open the app in your browser at http://localhost:5173.

### Key Features

Core Functionality:
- Compute average grades for students and modules.
- Record academic year for grades.
- Use H2 in-memory database (no data persistence needed for now).

Additional Features:
- Visualizations for grade distributions.
- Role-based access control (e.g., admin, student).
- CSV export functionality for grades.

Architecture

Insert UML Diagram Here

### Development Workflow

Process:
- Follow best coding practices to ensure maintainability and clarity.
- Embrace CRUD principles: Create, Read, Update, Delete functionality is foundational in both the backend and frontend.
- Practice Agile manifesto principles: prioritize collaboration, respond to change, and deliver working software incrementally.
- Use TDD (test-driven development) to guide implementation.
- Manage tasks and track progress through GitHub issues.
- Use pull requests for all changes to maintain code quality. No direct commits to main!

### Technical Details

Backend:
- Built with Spring Boot, featuring RESTful endpoints like /grades/addGrade.
- Database schema defined using schema.sql.
- Configured CORS for seamless frontend-backend integration.

Frontend:
- Built with React and TypeScript/JavaScript.
- Easy-to-use UI for grade management and visualization.
