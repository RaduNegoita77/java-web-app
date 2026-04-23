The goal of this project is to manage team members and their projects through a centralized server. It uses a custom-built Java server to handle HTTP requests and serve a web-based frontend.

Key Features
Auth: Simple Role-Based access (Admin vs. User).

Admin Panel: Create and manage user accounts.

Project Tracking: Users can create and link projects to their profiles.

Live Feed: Real-time project dashboard using PostgreSQL.

Tech Stack
Backend: Java (ServerSocket, Multithreading).

Frontend: HTML5, CSS (Glassmorphism style), JS (ES6).

Database: PostgreSQL.

Data Format: JSON for Client-Server communication.

 System Architecture
The application avoids using Spring or other frameworks to demonstrate low-level networking:

Multi-threaded Server: Each client connection is handled in a separate thread.

Input Validation: Server-side checks for null/empty values before DB insertion.

Persistence: Relational mapping to PostgreSQL.

 Getting Started
1. Database
Create a PostgreSQL database named proiect_facultate.

2. Run the App
Open the project in your IDE (IntelliJ/Eclipse).

Make sure the PostgreSQL driver is in your classpath (via pom.xml).

Run Main.java. The server starts on http://localhost:8080. 
