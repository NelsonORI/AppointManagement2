# Appointment Management System

An Android application built for BBT 3.2: Mobile Application Development at Strathmore University.

## Project Details
- **Theme:** Theme 1 – Health and Wellness for All
- **Domain:** Outpatient Clinic Appointment Management
- **Course:** BBT 3.2 Mobile Application Development
- **Target Platform:** Android (Min SDK 24, Target SDK 35, Compile SDK 35)

## Overview & Core Features
The Appointment Management System allows patients to schedule, view, reschedule, and manage clinic appointments while maintaining clinic contact accessibility:
- **Authentication:** Independent Login and Registration flows using explicit `Intent`s.
- **Dashboard:** Overview of pending appointments and quick-action shortcuts.
- **Booking Flow:** Form capturing patient name, contact number, doctor, date, time, and visit reason.
- **My Appointments:** Dynamic `RecyclerView` displaying active and historical appointments.
- **Appointment Management:** Update, reschedule, and delete operations on individual appointment records.
- **Direct Clinic Contact:** Implicit `Intent` trigger (`ACTION_DIAL`) to connect patients directly to the clinic hotline.

## Tech Stack & Architecture
- **Language:** Kotlin
- **UI Framework:** XML Layouts with `ConstraintLayout` as the root layout standard.
- **Binding:** Dual View Binding and Data Binding configurations.
- **Navigation:** Single-Activity navigation model (`MainActivity`) powered by Jetpack Navigation (`NavHostFragment`, `nav_graph.xml`, and Safe Args for type-safe arguments).
- **Navigation UI:** Custom Toolbar integration with options menu and a `DrawerLayout` (`NavigationView`).
- **Data Architecture:** In-memory repository pattern (`AppointmentRepository` singleton) serving as the single source of truth for CRUD operations before persistent database migration.

## Build & Environment Setup
- **IDE / Build Tools:** Android Studio with Android Gradle Plugin (AGP) `8.8.1`.
- **Pinned Dependencies:** Due to AGP 8.8.1 constraints, specific AndroidX libraries are pinned in `gradle/libs.versions.toml`:
    - `androidx.core:core-ktx:1.15.0`
    - `androidx.activity:activity:1.10.1`
- **Navigation & Safe Args:** Version `2.8.8` configured via Gradle Version Catalog.

## Git Workflow & Conventions
Commit messages adhere strictly to conventional commit formats:
- `feat`: New user features
- `build`: Dependency or build tool configuration
- `fix`: Bug resolution
- `docs`: Documentation updates
- `chore`: Maintenance tasks