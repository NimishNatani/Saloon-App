package com.practicecoding.sallonapp.appui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.BarberModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberServiceViewModel @Inject constructor(
    private val repo: FireStoreDbRepository,
) : ViewModel(){
     private val _barberList = mutableStateOf<List<BarberModel>>(emptyList())
     val barberList : State<List<BarberModel>> = _barberList

    suspend fun getBarberListByService(service: String){
        _barberList.value = repo.getBarberByService(service)
    }
}