package com.practicecoding.sallonapp.appui.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.auth.User
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.utils.showMsg
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repo:FireStoreDbRepository
) :ViewModel(){
    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = _loggedInUser


    fun addUserData(userModel: UserModel,activity:Activity)=
        viewModelScope.launch {
            repo.addUser(userModel).collect {
                when (it) {
                    is Resource.Success -> {
                        activity.showMsg(it.result)
                    }

                    is Resource.Failure -> {
                        activity.showMsg(it.exception.toString())
                    }

                    Resource.Loading -> {
                    }
                }
            }


        }}
//            if (result is Result<Resource.Success>) {
//                _loggedInUser.value = result.data
//            } else if (result is Result.Error) {
//                // Handle error
//            }
//        }

