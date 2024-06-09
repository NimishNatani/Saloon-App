package com.practicecoding.sallonapp.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class LocationModel(
    val latitude: String?,
    val longitude: String?,
    val city: String?,
    val state: String?,
    val country: String?
)

object locationObject{
    var locationDetails by mutableStateOf(LocationModel(null, null, null, null, null))

}