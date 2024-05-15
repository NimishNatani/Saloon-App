package com.practicecoding.sallonapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikedBarbersTable")
data class LikedBarber(

    @PrimaryKey val uid: String
)