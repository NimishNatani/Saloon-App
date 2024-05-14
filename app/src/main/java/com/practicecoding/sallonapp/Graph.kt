package com.practicecoding.sallonapp

import android.content.Context
import androidx.room.Room
import com.practicecoding.sallonapp.data.LikedBarberRepo
import com.practicecoding.sallonapp.data.model.LikedBarberDatabase

object Graph {

    fun provide(context : Context ){
        LikedBarberDb = Room.databaseBuilder(
            context,
            LikedBarberDatabase::class.java,
            "LikedBarber.db"
        ).build()
    }

    lateinit var LikedBarberDb : LikedBarberDatabase

    val likedBarberRepo by lazy {
        LikedBarberRepo(LikedBarberDb.likedBarberDao())
    }

}