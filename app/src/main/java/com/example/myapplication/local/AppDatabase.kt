package com.example.myapplication.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
