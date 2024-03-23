package com.practicecoding.sallonapp.data.model

data class BarberModel(
    val name: String? = "",
    val shopName: String? = "",
    val phoneNumber: String? = "",
    val saloonType: String? = "",
    var imageUri: String? = "",
    val shopAddress: String? = "",
    val review:Double =0.0,
    val city:String?=""
)