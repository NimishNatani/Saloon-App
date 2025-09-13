package com.practicecoding.sallonapp.appui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllUserDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel(){
    private var _user = MutableStateFlow(UserModel())
    var user  = _user.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUser()
        }
    }

    private suspend fun getCurrentUser() {
        viewModelScope.launch {
            _user.emit(repo.getUser()!!)
        }
    }
}