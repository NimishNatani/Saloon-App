package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ServiceModel(
    val name: String? = "",
    val price: String = "0",
    val time:String = "00:00"
): Parcelable
@Serializable
@Parcelize
data class ServiceCategoryModel(
    val type: String? = "",
    val services: List<ServiceModel> = emptyList()
): Parcelable
@Serializable
@Parcelize
data class Service(
    val serviceName: String="",
    var count: Int = 0,
    var price: String="",
    val time :String="",
    val id: String="",
    val type:String=""
    ): Parcelable