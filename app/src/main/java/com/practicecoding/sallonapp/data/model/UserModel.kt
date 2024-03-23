package com.practicecoding.sallonapp.data.model

import android.net.Uri
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.Gender

data class UserModel(
    val name:String ?="",
    val phoneNumber:String?="",
    val dateofBirth:String?="",
    val gender: String? ="",
    var imageUri: String? =""
)
