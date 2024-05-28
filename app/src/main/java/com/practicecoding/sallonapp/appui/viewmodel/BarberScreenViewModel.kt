package com.practicecoding.sallonapp.appui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BarberScreenViewModel:ViewModel() {
    var services = mutableStateOf<List<ServiceCat>>(emptyList())
    var isDialog = mutableStateOf(true)
    var isDataInitialized = mutableStateOf(false)


    fun initializeData(viewModelBarber:GetBarberDataViewModel,uid:String) {
        if (isDataInitialized.value) return

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isDialog.value = true
                delay(500)
                services.value = viewModelBarber.getServices(uid)
                isDialog.value = false
                isDataInitialized.value = true

            }
        }
    }

}