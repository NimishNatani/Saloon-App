package com.practicecoding.sallonapp.room

import android.content.Context
import androidx.room.Room

object Graph {
    @Volatile
    private  var LikedBarberDb : Database?=null

    fun provide(context : Context):Database{
        val tempInstance= LikedBarberDb
        if(tempInstance != null){
            return tempInstance
        }
        synchronized(this){
            val instance = Room.databaseBuilder(
                context = context.applicationContext,
                Database::class.java,
                "LikedBarber.db"
            ).build()
            LikedBarberDb=instance
            return instance
        }

    }


}