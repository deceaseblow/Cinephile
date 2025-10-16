package com.example.myapplication.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: MovieEntity)

    @Query("DELETE FROM watchlist_movies WHERE id = :movieId")
    suspend fun removeFromWatchlist(movieId: Int)

    @Query("SELECT * FROM watchlist_movies")
    fun getWatchlist(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_movies WHERE id = :movieId)")
    suspend fun isInWatchlist(movieId: Int): Boolean
}
