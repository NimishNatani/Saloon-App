package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceModel(
    val name: String? = "",
    val price: String = "0",
    val time:String = "00:00"
): Parcelable

@Parcelize
data class ServiceCat(
    val type: String? = "",
    val services: List<ServiceModel> = emptyList()
): Parcelable
@Parcelize
data class Service(
    val serviceName: String,
    var isServiceSelected: Boolean = false,
    var price: String,
//    val serviceTypeHeading: String,
    val time :String,
    val id: String,
    ): Parcelable