package com.example.myapplication.repository

import com.example.myapplication.model.Movie
import com.example.myapplication.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {

    suspend fun getMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        val response = ApiService.api.searchMovies(query)
        response.results.map {
            Movie(
                id = it.id,
                title = it.title,
                posterUrl = it.poster_path?.let { path -> "https://image.tmdb.org/t/p/w500$path" },
                synopsis = it.overview,
                releaseDate = it.release_date,
                director = null, // will be filled by getMovieDetails if needed
                cast = null,
                rating = it.vote_average
            )
        }
    }

    suspend fun getMovieDetails(movieId: Int): Movie = withContext(Dispatchers.IO) {
        val details = ApiService.api.getMovieDetails(movieId)
        val director = details.credits?.crew?.find { it.job == "Director" }?.name
        val castList = details.credits?.cast?.take(5)?.joinToString(", ") { it.name ?: "" }

        Movie(
            id = details.id,
            title = details.title,
            posterUrl = details.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
            synopsis = details.overview,
            releaseDate = details.release_date,
            director = director,
            cast = castList,
            rating = details.vote_average
        )
    }
}
