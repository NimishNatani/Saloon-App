package com.practicecoding.sallonapp.data

import android.app.Activity
import com.practicecoding.sallonapp.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FireStoreDbRepository {

    suspend  fun addUser(
        userModel: UserModel
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ) : Flow<Resource<String>>



}