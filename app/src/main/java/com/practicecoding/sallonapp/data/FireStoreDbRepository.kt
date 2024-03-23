package com.practicecoding.sallonapp.data

import android.app.Activity
import android.net.Uri
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FireStoreDbRepository {

    suspend fun addUser(
        userModel: UserModel,
        imageUri: Uri?
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ): Flow<Resource<String>>

    suspend fun getUser(
    ): UserModel?

    suspend fun getBarberPopular():MutableList<BarberModel>
    suspend fun getBarberNearby(city:String):MutableList<BarberModel>


}