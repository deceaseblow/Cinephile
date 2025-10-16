package com.example.myapplication.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String?,
    val rating: Float,
    val releaseDate: String? = null,
    val overview: String? = null,
    val genres: String? = null,
    val language: String? = null,
    val runtime: Int? = null,
    val director: String? = null,
    val cast: String? = null,
    val tagline: String? = null
)

