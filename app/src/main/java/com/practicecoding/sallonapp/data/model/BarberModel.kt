package com.practicecoding.sallonapp.data.model

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
    var noOfReviews: String? = "",
    var rating: Double = 0.0,
)