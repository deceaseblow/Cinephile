# Movie Explorer App

A modern Android application built with **Jetpack Compose**, **MVVM architecture**, and **TMDB API** to browse, search, and explore movies.  
Users can view details, rate films, and manage a personal **watchlist** — all with a sleek Material 3 UI.

## Features

-  **Search movies** by title or director using the TMDB API  
-  **Home screen** showing trending / popular movies  
-  **Detailed movie view** with poster, synopsis, release date, director & cast info  
-  **Rate movies** with half-star precision (0.5–5.0)  
-  **Add/remove from Favorites**  
-  **Add/remove from Watchlist** (stored locally with Room database)  
-  **Built entirely in Jetpack Compose (Material 3)**  
-  **MVVM pattern** with Kotlin coroutines and Retrofit

## Tech Stack

Layer - Libraries 
**UI** - Jetpack Compose, Material 3, Navigation Compose 
**Architecture**- MVVM (ViewModel, Repository, LiveData/Flow) 
**Networking** - Retrofit + Gson 
**Images** - Coil Compose 
**Database** - Room (for Wishlist persistence) 
**Coroutines** - Kotlinx Coroutines 
**API** - [The Movie Database (TMDB)](https://www.themoviedb.org/documentation/api)

## How to Setup !

### 1 : Get a TMDB API Key for free.
Create an account on [TMDB](https://www.themoviedb.org/) → go to **Settings → API** → generate an **API key (v3 auth)**.

### 2:  Add your API key securely.

In your **`local.properties`** file (in project root):

```properties
TMDB_API_KEY=your_tmdb_api_key_here
```

to be done : 
1. add to favorites, remove from favorites (works like adding to watchlist, and make another favorites screen)
2. rating adding to movies
3. quiz generator to be edited
