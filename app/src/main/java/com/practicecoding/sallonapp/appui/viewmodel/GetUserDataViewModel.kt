package com.practicecoding.sallonapp.appui.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUserDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private var _user = mutableStateOf(UserModel())
    var user: State<UserModel> = _user

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    init {
        viewModelScope.launch { getUser() }
    }

    suspend fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdateUser -> updateBarber(event.user, event.imageUri, event.context, event.navController)
            is UserEvent.GetUser -> getUser()
        }
    }
    suspend fun getUser() {
        viewModelScope.launch { _user.value= repo.getUser()!! }
    }
    private suspend fun updateBarber(user : UserModel, imageUri: Uri?, context: Context, navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            repo.updateUserInfo(user, imageUri).collect {
                    resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Info Updated Successfully", Toast.LENGTH_SHORT).show()
                        getUser()
                        navController.navigate(Screens.MainScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        _isLoading.value = false
                    }
                    is Resource.Failure -> {
                        getUser()
                       navController.popBackStack()
                        _isLoading.value = false
                        Toast.makeText(context, "Failed to Update Info", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}

sealed class UserEvent {
    data class UpdateUser(val user: UserModel, val imageUri: Uri?, val context: Context, val navController: NavController) : UserEvent()
    object GetUser : UserEvent()
}