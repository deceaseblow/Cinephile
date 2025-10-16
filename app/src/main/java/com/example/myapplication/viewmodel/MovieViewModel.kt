package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.local.MovieEntity
import com.example.myapplication.model.Movie
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application.applicationContext)

    private val _homeMovies = MutableStateFlow<List<Movie>>(emptyList())
    val homeMovies: StateFlow<List<Movie>> = _homeMovies

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults

    private val _watchlist = MutableStateFlow<List<MovieEntity>>(emptyList())
    val watchlist: StateFlow<List<MovieEntity>> = _watchlist

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Listen to local database updates
        viewModelScope.launch {
            repository.getWatchlist().collect { movies ->
                _watchlist.value = movies
            }
        }
    }

    fun fetchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _homeMovies.value = repository.getMovies(query)
            _isLoading.value = false
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _searchResults.value = repository.getMovies(query)
            _isLoading.value = false
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedMovie.value = repository.getMovieDetails(movieId)
            _isLoading.value = false
        }
    }
    fun toggleWatchlist(movieId: Int) {
        viewModelScope.launch {
            // Find the movie from anywhere
            val movie = _homeMovies.value.find { it.id == movieId }
                ?: _searchResults.value.find { it.id == movieId }
                ?: _selectedMovie.value
                ?: return@launch

            val isInList = repository.isInWatchlist(movie.id)
            if (isInList) repository.removeFromWatchlist(movie.id)
            else repository.addToWatchlist(movie)

            // Now reflect the change in UI
            val updatedHome = _homeMovies.value.map {
                if (it.id == movie.id) it.copy(isFavorite = !isInList) else it
            }
            val updatedSearch = _searchResults.value.map {
                if (it.id == movie.id) it.copy(isFavorite = !isInList) else it
            }

            _homeMovies.value = updatedHome
            _searchResults.value = updatedSearch

            if (_selectedMovie.value?.id == movie.id) {
                _selectedMovie.value = _selectedMovie.value?.copy(isFavorite = !isInList)
            }
        }
    }


    fun isInWatchlist(movieId: Int): Boolean {
        return _watchlist.value.any { it.id == movieId }
    }

    // --- Optional UI utilities ---
    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            _homeMovies.value = _homeMovies.value.map { movie ->
                if (movie.id == movieId) movie.copy(isFavorite = !movie.isFavorite)
                else movie
            }
        }
    }

    fun rateMovie(movieId: Int, rating: Float) {
        viewModelScope.launch {
            _homeMovies.value = _homeMovies.value.map { movie ->
                if (movie.id == movieId) movie.copy(userRating = rating)
                else movie
            }
        }
    }
}
