# MobileTech Utility App (Calorie Calculator)

## Overview

This project is a calorie calculator utility app developed for CP3406 Assessment 1.

The app allows users to search for food using the USDA FoodData Central API, select a food item, enter the food weight in grams, and calculate the estimated calorie content.

The main purpose of the app is to provide quick, at-a-glance calorie information with minimal user interaction.

---

## Getting Started

### How to Run
1. Clone or download this repo  
2. Open in Android Studio  
3. Run on an emulator or physical device (API 26+ recommended)  

---

## Core Features

- Search for food using the USDA FoodData Central API
- Display food search results with calories per 100g
- Select a food result to automatically load its calorie value
- Enter food weight in grams
- Calculate estimated calories based on food weight
- Display the calorie result in a Material Design card
- Show loading and error messages for API requests
- Scroll the main screen when search results require more space
- Settings screen with an option to show decimal calorie values
- Bottom navigation between the Utility and Settings screens

---

## How the Calorie Calculation Works

The calorie calculation uses the food's calorie value per 100 grams.

Formula:

  Estimated Calories = Calories per 100g × Food Weight / 100

Example:

- Calories per 100g: 165 kcal
- Food weight: 200 g
- Estimated calories: 330 kcal

---

## App Architecture

The project separates the user interface, application state, calculation logics, and networking responsibilities.

### MainActivity.kt

Contains the Jetpack Compose user interface, including:

- UtilityApp()
- UtilityScreen()
- SettingsScreen()
- Bottom navigation
- Food search controls
- Search result cards
- Weight input
- Calorie result display

### CalorieViewModel.kt

Manages the UI state and app logic.

Responsibilities include:

- storing food search input
- storing calorie and weight values
- calculating the current calorie result
- managing loading and error states
- performing food searches
- selecting food results
- managing the decimal display setting

### CalorieRepository.kt

Acts as the data layer between the ViewModel and external data source.

It is responsible for:

- calorie calculation support
- requesting food data from the API

### FoodApiService.kt

Defines the Retrofit request used to communicate with the USDA FoodData Central API.

### RetrofitClient.kt

Creates and configures the Retrofit client used by the app.

### FoodModels.kt

Contains the data classes used to represent food search responses and nutrient information returned by the API.

---

## API

The application uses the USDA FoodData Central API.

Food searches are performed using Retrofit, and the returned nutrient data is used to find the energy value in kilocalories.

The current project uses the USDA `DEMO_KEY` for API access.

Because the demo key has request limits, searches may temporarily fail if too many requests are made within a short period.

---

## Settings

The Settings screen currently includes:

### Show Decimal Calories

When disabled:

`330 kcal`

When enabled:

`330.0 kcal`

The setting immediately affects how the result is displayed on the Utility screen.

---

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- ViewModel
- StateFlow
- Repository pattern
- Kotlin Coroutines
- Retrofit
- Gson Converter
- USDA FoodData Central API
- GitHub

---

## Key Concepts Covered

| Week | Concept | Implementation |
|------|---------|----------------|
| 1 | Kotlin and Android Studio | Kotlin app development and Android Studio project setup |
| 2 | Jetpack Compose layouts | Column, Row, Scaffold, Cards, TextFields and modifiers |
| 3 | Material Design 3 | Material components, navigation, typography, spacing and cards |
| 4 | App architecture | ViewModel, StateFlow, Repository and separation of concerns |
| 5 | Web APIs | Retrofit and USDA FoodData Central API |

---

## Known Limitations

- The USDA `DEMO_KEY` has a limited number of API requests.
- Multiple USDA records may have similar or identical food descriptions.
- Some food records may not contain usable calorie data.
- The app estimates calories using the selected food's energy value and user-entered weight.
- Settings are not stored permanently after the app is closed.

---

## 📚 License
This template is provided for educational use in CP3406.  
