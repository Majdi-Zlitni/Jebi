# Jebi - Wallet Management App

## Overview

Jebi is a fully functional Android application built with Kotlin and Jetpack Compose. It is designed to help users manage their wallets, track transactions, and set financial goals.

## Features Implemented

Based on the project issues, the following modules have been implemented:

### 1. User Authentication & Security (Issue #2)

- **Login Screen**: User interface for logging in.
- **Register Screen**: User interface for creating a new account.
- **AuthViewModel**: Handles authentication logic (currently mocked for local development).
- **Navigation**: Seamless transition between login, register, and dashboard screens.

### 2. Wallet Management Core Module (Issue #3)

- **Dashboard**: Displays a list of user wallets with their balances.
- **Add Wallet**: Allows users to create new wallets with custom names, currencies, and types.
- **Local Database**: Uses **Room Database** to persist wallet data locally.
- **Repository Pattern**: Clean architecture implementation using Repositories and ViewModels.
- **Dependency Injection**: Uses **Hilt** for dependency injection.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Local Storage**: Room Database
- **Navigation**: Jetpack Navigation Compose
- **Asynchronous Programming**: Coroutines & Flow

## Project Structure

```
com.esprit.jebi
├── data
│   ├── local           # Room Database, DAOs, Entities
│   └── repository      # Repositories
├── di                  # Hilt Modules
├── ui
│   ├── auth            # Authentication Screens & ViewModel
│   ├── navigation      # Navigation Graph & Routes
│   ├── theme           # App Theme & Colors
│   └── wallet          # Wallet Management Screens & ViewModel
├── JebiApp.kt          # Application Class
└── MainActivity.kt     # Entry Point
```

## Getting Started

1. Open the project in Android Studio.
2. Ensure JDK 17 is configured (File > Settings > Build Tools > Gradle > Gradle JDK).
3. Use the provided Gradle wrapper (`gradlew` / `gradlew.bat`). First sync Gradle files.
4. Build and run:

```
./gradlew assembleDebug   # macOS/Linux
gradlew.bat assembleDebug # Windows
```

5. Run on an emulator or physical device.

If you see configuration errors referencing `Configuration.fileCollection(Spec)`, verify you are NOT using a Gradle 9 milestone. The wrapper targets Gradle 8.4 which is compatible with AGP 8.2.0.
