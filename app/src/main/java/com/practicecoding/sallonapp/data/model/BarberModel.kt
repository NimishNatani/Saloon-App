package com.practicecoding.sallonapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class BarberModel(
    val name: String? = "",
    val shopName: String? = "",
    val phoneNumber: String? = "",
    val saloonType: String? = "",
    var imageUri: String? = "",
    val shopStreetAddress: String? = "",
    var city: String? = "",
    var state: String? = "",
    var aboutUs: String? = "",
    var noOfReviews: String? = "0",
    var rating: Double = 0.0,
    val uid:String,
    val lat:Double,
    val long:Double,
    val open:Boolean?=false
)

@Entity(tableName = "LikedBarbersTable")
data class LikedBarber(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 1,
    @ColumnInfo(name = "BarberUid") val barberUid : String = " "
)