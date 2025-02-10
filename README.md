# OAuth2 Social Login

A Spring Boot application with OAuth2 integration (Facebook and GitHub) for social logins. Features include custom user management, multi-factor authentication (MFA) via email OTP, secure session handling, and enhanced security measures like rate-limiting and event logging. Built with Thymeleaf for a seamless user interface.

## Features

### Authentication & Authorization
- **User Registration** (`/register`) - Users can register with a username, email, and password.
- **Password-based Login** (`/login`) - Users log in with their credentials and receive an OTP via email.
- **OAuth2 Login** - Users can authenticate using **GitHub** or **Facebook** without OTP verification.

### Multi-Factor Authentication (MFA)
- **OTP Generation** (`/otp/create`) - An email-based OTP is generated after a successful password login.
- **OTP Validation** (`/otp/validate`) - Users must enter the OTP to access the dashboard.

### Security Enhancements
- **Rate-Limiting Login Attempts** - Prevents brute-force attacks by blocking excessive failed login attempts.
- **Event Logging** - Tracks login failures, MFA failures, and other security events.

### Session Management
- **Logout Functionality** - Users can securely log out, and sessions are invalidated upon logout.

### Front-End Integration
- **Thymeleaf UI** - A simple and functional front-end for login and MFA verification.

## Tech Stack
- **Spring Boot** (Backend Framework)
- **Spring Security** (Authentication & Authorization)
- **OAuth2** (GitHub & Facebook authentication)
- **Thymeleaf** (Server-side rendering for UI)
- **H2 Database** (In-memory storage for development)
- **Junit & Mockito** (Unit and integration testing)
- **MailHog** (Mock email server for OTP delivery)
- **Maven** (Build and dependency management)

## Installation & Setup

### Prerequisites
- Java 17+
- Maven
- MailHog (for testing email OTP)
- Facebook & GitHub OAuth credentials

### Clone the Repository
```sh
git clone https://github.com/Nich0las2004/oauth2-social-login.git
cd oauth2-social-login
```

### Configure Environment Variables
In **application.properties** configure following variables: **DB_URL**, **DB_USERNAME**, **DB_PASSWORD**, **GITHUB_CLIENT_ID**, **GITHUB_CLIENT_SECRET**, **FACEBOOK_CLIENT_ID**,   **FACEBOOK_CLIENT_SECRET**, **MAIL_PASSWORD**, **MAIL_USERNAME**.


## Usage

### Register a New User

Register on **/register** with username,email and password

### Login with Username & Password

If successful, an OTP is sent via email.

- If successful, an OTP is sent via email.
- User must verify the OTP to access the dashboard.

### Login via OAuth2 (GitHub/Facebook)

- **/login** has buttons for both oauth2 authentication, github and facebook
- No OTP is required for OAuth2 logins.
- if successful, user is redirected to **/dashboard**.

### Verify OTP

- verify the OTP sent though email

### Logout

- logout is possible from both normal login and oauth2 logins. with a button.