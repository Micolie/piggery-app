# Piggery App

A native Android application for managing pig farm operations, including inventory registration, photo documentation, and offline data storage.

## Features

### Pig Inventory & Registration ✅ (Implemented)

- **Pig Registration**: Record individual pigs with comprehensive details
  - Unique tag/ID number with validation
  - Breed information
  - Gender (Male/Female)
  - Date of birth with age calculation
  - Weight tracking (in kg)
  - Status management (Active, Sold, Deceased, Quarantine)
  - Notes for additional information

- **Photo Documentation**: Capture and store pig photos
  - Camera integration for taking photos
  - Photo storage with automatic rotation handling
  - Image compression for efficient storage
  - Photo removal capability

- **Offline Capability**: Full offline support using Room database
  - SQLite local database
  - No internet connection required
  - Fast data access and retrieval

- **Search & Filter**:
  - Search pigs by tag number or breed
  - Filter by status
  - Sort by registration date

- **Data Management**:
  - Add new pigs
  - Edit existing pig records
  - Delete pig records
  - Automatic timestamp tracking

## Technology Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **UI**: Material Design Components, ViewBinding
- **Photo Handling**: CameraX, Glide
- **Concurrency**: Kotlin Coroutines
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
app/src/main/java/com/piggery/app/
├── data/
│   ├── dao/           # Data Access Objects
│   ├── database/      # Room Database setup
│   ├── entity/        # Data models
│   └── repository/    # Repository pattern implementation
├── ui/
│   ├── pig/
│   │   ├── list/      # Pig list UI and adapter
│   │   └── register/  # Pig registration UI
│   └── MainActivity.kt
├── util/              # Utility classes
├── viewmodel/         # ViewModels
└── PiggeryApplication.kt
```

## Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## Permissions Required

- **Camera**: For taking pig photos
- **Storage**: For saving and loading photos (API < 29)

## Future Features (Not Yet Implemented)

- Farrowing Management - Track births, litters, sows
- Weaning - Track when piglets are weaned from mothers
- Feeding - Feeding schedules and tracking
- Health Management - Vaccinations, treatments, medical records
- Cloud Sync - Backup and sync data across devices
- Reports & Analytics - Statistical insights and reports
- Export Data - Export to CSV/Excel

## Database Schema

### Pig Entity
- `id`: Primary key (auto-generated)
- `tagNumber`: Unique identifier (indexed)
- `breed`: Breed name
- `gender`: MALE or FEMALE
- `dateOfBirth`: Birth date (timestamp)
- `weight`: Weight in kg
- `status`: ACTIVE, SOLD, DECEASED, or QUARANTINE
- `photoPath`: File path to pig photo (optional)
- `notes`: Additional notes (optional)
- `registeredDate`: Registration timestamp
- `lastUpdated`: Last modification timestamp

## Development Notes

- Uses AndroidX libraries
- Follows Material Design guidelines
- Implements proper error handling and validation
- Photo files are stored in app-private storage
- Database supports future migrations
- Unique constraint on tag numbers

## License

This project is for educational and farm management purposes.
