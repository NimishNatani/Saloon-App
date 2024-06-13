package com.practicecoding.sallonapp.appui.viewmodel

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.data.model.locationObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private var barberPopular = mutableStateOf<List<BarberModel>>(emptyList())
    var _barberPopular: State<List<BarberModel>> = barberPopular

    private var barberNearby = mutableStateOf<List<BarberModel>>(emptyList())
    var _barberNearby: State<List<BarberModel>> = barberPopular

    private var services = mutableStateOf<List<ServiceCat>>(emptyList())
    var _services: State<List<ServiceCat>> = services

    var genderCounter = mutableStateOf(value = listOf(0, 0, 0))

    var listOfService = mutableStateOf<List<Service>>(emptyList())

    var selectedSlots = mutableStateListOf<TimeSlot>()

    private var slots = mutableStateOf(Slots("08:00", "22:00"))
    var _slots: State<Slots> = slots

    suspend fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.getBarberNearby -> getBarberNearby(event.city, event.limit)
            is MainEvent.getBarberPopular -> getBarberPopular(event.city, event.limit)
            is MainEvent.getServices -> getServices(event.uid)
            is MainEvent.getSlots -> getSlots(event.day, event.uid)
            is MainEvent.setBooking -> setBooking(
                event.barberuid,
                event.useruid,
                event.service,
                event.gender,
                event.date,
                event.times
            )

            else -> {}
        }
    }

    private suspend fun getBarberPopular(city: String, limit: Long) {
        viewModelScope.launch {
            barberPopular.value = repo.getBarberPopular(city, limit)
            barberPopular.value = barberPopular.value.map { barber ->
                barber.apply {
                    val locationDetails = locationObject.locationDetails
                    distance = getLocation(
                        lat1 = locationDetails.latitude!!.toDouble(),
                        long1 = locationDetails.longitude!!.toDouble(),
                        lat2 = barber.lat,
                        long2 = barber.long
                    )
                }
            }
        }
    }

    private suspend fun getBarberNearby(city: String, limit: Long) {
        viewModelScope.launch {
            barberNearby.value = repo.getBarberNearby(city, limit)
            barberPopular.value = barberNearby.value.map { barber ->
                barber.apply {
                    val locationDetails = locationObject.locationDetails
                    distance = getLocation(
                        lat1 = locationDetails.latitude!!.toDouble(),
                        long1 = locationDetails.longitude!!.toDouble(),
                        lat2 = barber.lat,
                        long2 = barber.long
                    )
                }
            }.sortedBy { it.distance }
        }
    }

    suspend fun getBarber(uid: String?) = repo.getBarber(uid)
    private suspend fun getServices(uid: String?) {
        viewModelScope.launch { services.value = repo.getServices(uid) }
    }

    private fun getLocation(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val distance: FloatArray =
            FloatArray(1)
        Location.distanceBetween(lat1, long1, lat2, long2, distance)
        var solution = distance[0].toDouble() / 1000
        solution = Math.round(solution * 10.0) / 10.0
        return solution
    }

    suspend fun getSlots(day: String, uid: String) {
        viewModelScope.launch { slots.value = repo.getTimeSlot(day, uid) }
    }

    fun initializedServices(serviceCat: List<ServiceCat>) {
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

    fun updateService(updatedService: Service) {
        listOfService.value = listOfService.value.map {
            if (it.id == updatedService.id) updatedService else it
        }
    }

    suspend fun setBooking(
        barberuid: String,
        useruid: String,
        service: List<Service>,
        gender: List<Int>,
        date: LocalDate,
        times: MutableState<List<TimeSlot>>
    ) {
        repo.setBooking(barberuid,useruid,service,gender,date.toString(), times)
    }

}

sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent()
    data class getServices(val uid: String) : MainEvent()
    data class getSlots(val day: String, val uid: String) : MainEvent()
    data class setBooking(
        val barberuid: String,
        val useruid: String,
        val service: List<Service>,
        val gender: List<Int>,
        val date: LocalDate,
        val times: MutableState<List<TimeSlot>>
    ) : MainEvent()
}