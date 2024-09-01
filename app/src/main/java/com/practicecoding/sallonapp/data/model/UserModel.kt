package com.practicecoding.sallonapp.data.model

data class UserModel(
    val name: String? = "",
    val phoneNumber: String? = "",
    val dateOfBirth: String? = "",
    val gender: String? = "",
    var imageUri: String? = "",
    val address: String? = "",
    val city: String? = "",
    val state: String? = ""
)
