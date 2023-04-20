# REACHED: Attendance Reporting Android Application
REACHED is an Android application designed to streamline attendance reporting and improve communication between teachers and parents in educational institutions. The app helps users efficiently manage daily attendance, reducing the time and resources required for attendance tracking.

## Features
- **Attendance Recording**: Real-time attendance tracking and reporting for teachers and parents, allowing identification of absenteeism patterns and timely interventions.
- **Two-Way Messaging**: Real-time communication between parents and teachers, integrated with Firebase Realtime Database.
- **Authentication**: A secure login system using Google Authentication and Firebase Authentication.
- **Role-Based Login**: Customized access and permissions for parents, teachers, and admins, ensuring data protection and efficient app usage.
- **Real-Time Notification**: Instant alerts powered by Firebase Cloud Messaging for absence reports and message notifications.

## Getting Started
These instructions will help you set up the project on your local machine for development and testing purposes.

### Installation
- Clone the repository: https://github.com/yourusername/REACHED.git
- Open the project in Android Studio.
- Sync Gradle files and build the project.

### Built With
- **Kotlin** - The programming language used
- **Javascript** - The progamming language used for the Google Cloud Functions (separate repository)
- **Android Studio** - The development environment
- **Firebase Realtime Database** - Data storage and synchronization
- **Firebase Authentication** - User authentication
- **Google OAuth2** - Secure Google sign-in
- **Google Cloud Functions** - Decouple the notification system from the application
