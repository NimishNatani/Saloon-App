package com.practicecoding.sallonapp.appui.viewmodel

import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.SlotsRepository
import com.practicecoding.sallonapp.data.model.DateSlots
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RestScreenViewModel @Inject constructor(
    private val repo: SlotsRepository
) : ViewModel(){
    var genderCounter = mutableStateOf<List<Int>>(value = listOf(0,0,0))
    var listOfService = mutableStateOf<List<Service>>(emptyList())
    var selectedSlots = mutableStateListOf<TimeSlot>()
    var slots = mutableStateOf(Slots("08:00","22:00"))

    fun initializedServices(serviceCat:List<ServiceCat>){
        if (listOfService.value.isEmpty()) {
            val initialServices = serviceCat.flatMap { servicecat ->
                servicecat.services.map { serviceModel ->
                    Service(
                        serviceName = serviceModel.name ?: "",
                        count = 0, // Initialize count to 0
                        price = serviceModel.price,
                        time = serviceModel.time,
                        id = serviceModel.name ?: "",
                        type = servicecat.type ?: ""
                    )
                }
            }
            listOfService.value = initialServices
        }

    }

    suspend fun getSlots(day:String, uid:String, slotsViewModel: SlotsViewModel ):Slots{


             return   slotsViewModel.getSlots(day, uid)


    }
    fun updateService(updatedService: Service) {
        listOfService.value = listOfService.value.map {
            if (it.id == updatedService.id) updatedService else it
        }
    }
}