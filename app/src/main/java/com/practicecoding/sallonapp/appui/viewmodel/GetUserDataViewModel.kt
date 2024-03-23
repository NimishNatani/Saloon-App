package com.practicecoding.sallonapp.appui.viewmodel

import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetUserDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel(){

     suspend fun getUser()=repo.getUser()
}