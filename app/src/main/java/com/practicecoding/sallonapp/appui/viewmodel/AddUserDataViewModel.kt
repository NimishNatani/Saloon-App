package com.practicecoding.sallonapp.appui.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddUserDataViewModel @Inject constructor(
    private val repo:FireStoreDbRepository
) :ViewModel() {
suspend fun addUserData(userModel: UserModel, imageUri: Uri?, activity: Activity) = repo.addUser(userModel,imageUri)

}

