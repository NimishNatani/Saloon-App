package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
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
    val uid:String="",
    val lat:Double=0.0,
    val long:Double=0.0,
    val open:Boolean?=false,
    var distance:Double?=0.0
): Parcelable

