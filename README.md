# Concert Finder

## 🎵 Find Concerts Near You\!

Concert Finder is a native Android application built with **Kotlin** and **Jetpack Compose**, designed to help you easily discover concerts and events happening in your vicinity. Whether you're looking for a big arena show or a local band at a small venue, Concert Finder aims to connect you with the music you love.

## ✨ Features

  * **Location-Based Search:** Find events near your current location using the Google Geocoding API.
  * **Comprehensive Event Details:** View information about concerts, including date, time, venue, and artists, powered by the Ticketmaster Discovery API.
  * **Save Events Locally:** Store your favorite events for offline access and quick retrieval using **Room Persistence Library**.
  * **User-Friendly Interface:** A modern and intuitive design built with Jetpack Compose.


## 📸 Screenshots

<img src="screenshots/EventList.png" alt="Events" width="200"> <img src="screenshots/EventDetails.png" alt="Event Details" width="200"> <img src="screenshots/SearchScreen.png" alt="Search" width="201"> <img src="screenshots/FilterSelect.png" alt="Filter Select" width="198">


## 🚀 Technologies and Architecture

Concert Finder is built with a focus on modern Android development practices, leveraging the following key technologies and architectural principles:

  * **Kotlin:** The primary programming language for Android development.
  * **Jetpack Compose:** Android's modern toolkit for building native UI.
  * **Retrofit:** A type-safe HTTP client for Android and Java to consume the Ticketmaster Discovery API.
  * **GSON:** Used for serializing and deserializing JSON data from the API responses.
  * **Room Persistence Library:** For local data storage, including caching and saving user-favorite events.
  * **Ticketmaster Discovery API:** The primary source for concert and event data.
  * **Google Geocoding Library:** To convert location names into geographical coordinates for event searches.
  * **Hilt:** For robust and scalable dependency injection, ensuring a modular and testable codebase.
  * **Clean Architecture:** The project follows a clean architecture approach, separating concerns into data, domain, and presentation layers.
  * **MVVM (Model-View-ViewModel):** The architectural pattern used for building a reactive and maintainable UI.
  * **Android Studio:** The official IDE for Android development.
  * **Gradle:** Build automation tool.

## 📱 Installation and Setup

To get Concert Finder up and running on your local machine, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/carlsonn415/ConcertFinder.git
    ```
2.  **Open in Android Studio:**
      * Launch Android Studio.
      * Select `File > Open` and navigate to the cloned `ConcertFinder` directory.
      * Click `Open`.
3.  **Add API Keys and configure gradle:**
      * You'll need a **Ticketmaster Discovery API Key**.
      * Create a `gradle.properties` file in the root directory and add your key like this (example):
        ```properties
        API_KEY=YOUR_TICKETMASTER_API_KEY
        ```
      * As well as these lines:
        ```
        # Project-wide Gradle settings.
        org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
        android.useAndroidX=true
        kotlin.code.style=official
        android.nonTransitiveRClass=true
        ```
      * *Note: Ensure your key is not committed to your public repository for security reasons.*
4.  **Sync Gradle:**
      * Android Studio should automatically sync the Gradle project. If not, click `File > Sync Project with Gradle Files`.
5.  **Run on Device/Emulator:**
      * Connect an Android device to your computer with USB debugging enabled, or set up an Android Emulator.
      * Select your target device/emulator from the toolbar.
      * Click the `Run` button (green triangle) to build and install the app.

## 🤝 Contributing

Concert Finder is currently under active development. Contributions are welcome\! If you'd like to contribute, please follow these steps:

1.  **Fork the repository.**
2.  **Create a new branch:**
    ```bash
    git checkout -b feature/your-feature-name
    ```
3.  **Make your changes and commit them:**
    ```bash
    git commit -m "Add new feature: your feature description"
    ```
4.  **Push to your fork:**
    ```bash
    git push origin feature/your-feature-name
    ```
5.  **Open a Pull Request** explaining your changes.

Please make sure your code adheres to Kotlin best practices, follows the established clean architecture and MVVM principles, and includes appropriate tests if applicable.

-----

## 📄 License

```
Copyright 2025 Nathan Carlson

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
