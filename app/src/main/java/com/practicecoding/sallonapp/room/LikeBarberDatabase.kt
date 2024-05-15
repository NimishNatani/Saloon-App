package com.practicecoding.sallonapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LikedBarber::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun likedBarberDao(): Dao
}