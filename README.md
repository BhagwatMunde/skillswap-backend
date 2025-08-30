# skillswap-backend
SkillSwap - Peer-to-Peer Skill Exchange Platform
SkillSwap is a comprehensive, full-stack web application designed to facilitate a community-driven learning environment. It allows users to offer skills they can teach and find peers to learn new skills from, with a unique emphasis on verifying user expertise through a multi-faceted system.

The platform includes real-time features like notifications and one-to-one video/audio sessions for a complete, interactive learning experience.

Key Features
User Authentication & Profiles: Secure registration and login with JWT. Users can manage their profiles, including bio, skills they can teach, and skills they want to learn.

Two-Way Skill Matching: A dynamic "Find a Match" system that allows users to search for others who can teach a skill they want to learn, or find users who want to learn a skill they can teach.

Request & Session Management: Users can send, receive, and manage skill exchange requests. Once a request is accepted, they can schedule a session at a mutually agreed-upon time.

Skill Verification Module: The core unique feature of the platform.

Peer Reviews: Users can rate and review each other after a completed session.

Skill Tests: Admins can create simple quizzes for popular skills to formally verify a user's knowledge.

Endorsements: Users can endorse each other for specific skills, adding another layer of community-driven validation.

Real-Time Notifications: A built-in notification system alerts users to new requests, status updates, and upcoming sessions.

Live Video/Audio Sessions: Once a session is scheduled, users can join a private, one-to-one session room featuring real-time screen sharing and audio communication, powered by WebRTC.

Admin Dashboard: A secure admin panel for managing users (banning/unbanning) and the master list of skills available on the platform.

Analytics Dashboard: Provides users with insights into their activity, such as total sessions completed, and shows which skills are currently most in-demand on the platform.

Technology Stack
Backend
Framework: Spring Boot 3

Language: Java 17

Security: Spring Security with JWT (JSON Web Tokens)

Database: MySQL with Spring Data JPA (Hibernate)

Real-Time: Spring WebSockets for the signaling server

Build Tool: Maven

Frontend
Framework: React 18

Language: JavaScript (ES6+)

UI Library: Material-UI (MUI)

State Management: React Context API

Routing: React Router

API Communication: Axios

Getting Started
Prerequisites
Java 17+

Node.js & npm

MySQL Server

Backend Setup
Clone the repository.

Configure the application.properties file with your MySQL database credentials.

Run the application using your IDE or via the command line: mvn spring-boot:run.

The backend will start on http://localhost:8080.

Frontend Setup
Navigate to the skillswap-frontend directory.

Install dependencies: npm install.

Start the development server: npm start.

The frontend will be available at http://localhost:3000.
