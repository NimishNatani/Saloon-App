package com.practicecoding.sallonapp.data.model

import android.net.Uri
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.Gender

data class UserModel(
    val Name:String ?="",
    val PhoneNumber:String?="",
    val DateofBirth:String?="",
    val Gender: String? ="",
    var ImageUri: String? =""
)
