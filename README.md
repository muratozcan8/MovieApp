# MovieApp

MovieApp is an Android application that allows users to explore movies, view details, and save their favorite movies. This project demonstrates the use of modern Android development tools and practices, including MVVM architecture, Hilt for dependency injection, Room for local data storage, Retrofit for API requests, and xml for the UI.

## Screenshots
<img src="https://github.com/user-attachments/assets/7185ad53-020c-4494-b5f1-052f99145bd1" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/a683ab7a-764a-4d97-8241-7548cf898a9d" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/07170277-88d2-440b-aa71-b35b091e208d" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/a24b2c45-3fba-4222-8da7-38c24fb229ec" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/ae67ea1d-07c9-4763-9529-fc4e5c1ca4a5" alt="App Screenshot" width="300"/>
<img src="https://github.com/user-attachments/assets/bc26f296-58bf-4f5a-8bd2-35a2dc4afda2" alt="App Screenshot" width="300"/>


## Features

- **Browse Popular Movies**: Explore a list of popular movies fetched from the MovieDB API.
- **Search Movies**: Search for movies by title.
- **View Movie Details**: Click on any movie to view detailed information, including ratings and reviews.
- **Favorites**: Save your favorite movies for easy access later.
- **Responsive UI**: The app is designed to work on different screen sizes and orientations.

## Technologies Used

- **Kotlin**: Programming language.
- **XML**: For building the UI.
- **MVVM Architecture**: Model-View-ViewModel architecture pattern.
- **Hilt**: Dependency injection.
- **Retrofit**: For making HTTP requests to the MovieDB API.
- **Room**: Local database for saving favorite movies.
- **Paging 3**: For efficient pagination of movie lists.
- **Coroutines**: For handling asynchronous tasks.

## Setup Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/muratozcan8/MovieApp.git
    ```

2. **Open the project in Android Studio**:
   - Make sure you have the latest version of Android Studio installed.

3. **Add your API key**:
   - Obtain an API key from [The Movie Database (TMDB)](https://www.themoviedb.org/documentation/api).
   - Add your API key to the `local.properties` file:
     ```
     BEARER=your_bearer_key_here
     ```

4. **Build the project**:
   - Sync the project with Gradle files and build it.

5. **Run the app**:
   - Select a device or emulator and run the app.

