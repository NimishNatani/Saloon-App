package com.practicecoding.sallonapp.data.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicecoding.sallonapp.domain.LikedBarberDao

@Database(
    entities = [LikedBarber::class],
    version = 1,
    exportSchema = false
         )
abstract class LikedBarberDatabase : RoomDatabase() {
    abstract fun likedBarberDao(): LikedBarberDao
}