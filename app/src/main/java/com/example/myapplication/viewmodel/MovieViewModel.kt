package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Movie
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    fun fetchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _movies.value = repository.getMovies(query)
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

    fun toggleFavorite(movieId: Int) {
        _movies.value = _movies.value.map {
            if (it.id == movieId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun toggleWatchlist(movieId: Int) {
        _movies.value = _movies.value.map {
            if (it.id == movieId) it.copy(inWatchlist = !it.inWatchlist) else it
        }
    }

    fun rateMovie(movieId: Int, rating: Float) {
        _movies.value = _movies.value.map {
            if (it.id == movieId) it.copy(userRating = rating) else it
        }
    }
}
