package com.practicecoding.sallonapp.appui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
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
    suspend fun getUser() {
        viewModelScope.launch { _user.value= repo.getUser()!! }
    }
}