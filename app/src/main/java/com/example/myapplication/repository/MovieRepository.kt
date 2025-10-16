package com.example.myapplication.repository

import android.content.Context
import androidx.room.Room
import com.example.myapplication.local.AppDatabase
import com.example.myapplication.local.MovieEntity
import com.example.myapplication.model.Movie
import com.example.myapplication.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "movies_db"
    )
        .fallbackToDestructiveMigration() // Auto-delete and rebuild DB if schema changes
        .build()

    private val movieDao = db.movieDao()
//API FUNCTISNS
    suspend fun getMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        val response = ApiService.api.searchMovies(query)
        response.results.map {
            Movie(
                id = it.id,
                title = it.title,
                posterUrl = it.poster_path?.let { path -> "https://image.tmdb.org/t/p/w500$path" },
                synopsis = it.overview,
                releaseDate = it.release_date,
                director = null,
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

    suspend fun addToWatchlist(movie: Movie) {
        movieDao.addToWatchlist(
            MovieEntity(
                id = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl,
                rating = movie.rating
            )
        )
    }

    suspend fun getSimilarMovies(movieId: Int): List<Movie> = withContext(Dispatchers.IO) {
        val response = ApiService.api.getSimilarMovies(movieId)
        response.results.map {
            Movie(
                id = it.id,
                title = it.title,
                posterUrl = it.poster_path?.let { path -> "https://image.tmdb.org/t/p/w500$path" },
                synopsis = it.overview,
                releaseDate = it.release_date,
                director = null,
                cast = null,
                rating = it.vote_average
            )
        }
    }


    suspend fun removeFromWatchlist(movieId: Int) {
        movieDao.removeFromWatchlist(movieId)
    }

    fun getWatchlist(): Flow<List<MovieEntity>> = movieDao.getWatchlist()

    suspend fun isInWatchlist(movieId: Int): Boolean = movieDao.isInWatchlist(movieId)
}
