package com.example.myapplication.network

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

// Retrofit API interface
interface TmdbApi {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("append_to_response") append: String = "credits"
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse
}

// Data classes for TMDb responses
data class MovieResponse(val results: List<MovieResult>)

data class MovieResult(
    val id: Int,
    val title: String,
    val overview: String?,
    val release_date: String?,
    val poster_path: String?,
    val vote_average: Float
)

data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val overview: String?,
    val release_date: String?,
    val poster_path: String?,
    val vote_average: Float,
    val credits: Credits?
)

data class Credits(
    val cast: List<CastMember>?,
    val crew: List<CrewMember>?
)

data class CastMember(val name: String?)
data class CrewMember(val job: String?, val name: String?)

object ApiService {
    private val client = OkHttpClient.Builder().build()

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}
