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

    suspend fun getRecommendationsForMovie(movieId: Int): List<Movie> {
        return repository.getSimilarMovies(movieId)
    }

    fun addToWatchlist(movie: Movie) {
        viewModelScope.launch {
            repository.addToWatchlist(movie)
        }
    }

    fun removeFromWatchlist(movieId: Int) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movieId)
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
