# Library Management System

A full-stack Library Management System built with Spring Boot and React.

The application provides REST APIs for managing books, user authentication,
role-based authorization, book borrowing and returning, search, pagination,
sorting, and inventory tracking.

The backend is built with Spring Boot and deployed on Railway, while the
React frontend is deployed on Vercel.

## Live Demo

- **Frontend:** https://library-management-frontend-swart-seven.vercel.app/login
- **Backend API:** https://library-management-backend-production-2dc0.up.railway.app/auth/login

- **Swagger API Documentation:** https://library-management-backend-production-2dc0.up.railway.app/swagger-ui/index.html

## Demo Credentials

### Admin

- Username: `admin`
- Password: `admin123`

Admin users can manage books, including adding, updating, deleting books, borrow available books, return borrowed books,
and view their borrowing history.

### User

- Username: `john`
- Password: `john123`

Regular users can browse books, borrow available books, return borrowed books,
and view their borrowing history.

## Features

### Authentication & Authorization

- User registration and login
- JWT-based authentication
- BCrypt password hashing
- Role-based authorization
- Admin and User roles
- Protected REST endpoints
- Custom `401 Unauthorized` and `403 Forbidden` responses

### Book Management

- Add books
- View books
- View a book by ID
- Update book details
- Delete books
- Track total and available copies

### Search & Pagination

- Search books by title
- Search books by author
- Search by title and author together
- Pagination
- Sorting by supported fields
- Ascending and descending sorting
- Validation of sorting fields

### Borrowing Management

- Borrow available books
- Admin and User borrowing support
- Prevent borrowing when no copies are available
- Prevent a user from borrowing the same book twice
- Return borrowed books
- Prevent duplicate returns
- View personal borrowing history
- Detect overdue books
- Automatically update book availability
- Track total and available book copies

### Validation & Error Handling

- Request validation using Jakarta Bean Validation
- Global exception handling
- Structured error responses
- Meaningful HTTP status codes
- Custom business exceptions

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- Jakarta Bean Validation
- Maven
- MySQL

### Frontend

- React
- React Router
- Vite
- JavaScript
- CSS

### Deployment & Tools

- Docker
- Railway
- Vercel
- Swagger / OpenAPI
- Git
- GitHub
