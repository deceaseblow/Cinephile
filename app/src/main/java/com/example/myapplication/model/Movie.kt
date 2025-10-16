package com.example.myapplication.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val synopsis: String?,
    val releaseDate: String?,
    val director: String?,
    val cast: String?,
    val rating: Float,
    var userRating: Float? = null,
    var isFavorite: Boolean = false,
    var inWatchlist: Boolean = false
)
