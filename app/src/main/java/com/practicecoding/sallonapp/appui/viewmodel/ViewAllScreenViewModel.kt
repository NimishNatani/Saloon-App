package com.practicecoding.sallonapp.appui.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.model.BarberModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAllScreenViewModel : ViewModel() {
    var barbers = mutableStateOf<List<BarberModel>>(emptyList())
    var isDialog = mutableStateOf(true)
    private var isDataInitialized = mutableStateOf(false)

    fun initializeBarber(
        viewModelBarber: GetBarberDataViewModel,
        location: String,
        type: String,
        latitude: Double,
        longitude: Double,
    ) {
        if (isDataInitialized.value) return

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isDialog.value = true
                delay(500)
                if (type == "NearBy") {
                    barbers.value =
                        viewModelBarber.getBarberNearby(location, 15)
                    barbers.value = barbers.value.map { barber ->
                        barber.apply {
                            distance = getLocation(
                                lat1 = latitude,
                                long1 = longitude,
                                lat2 = barber.lat,
                                long2 = barber.long
                            )
                        }
                    }.sortedBy { it.distance }
                } else {
                    barbers.value = viewModelBarber.getBarberPopular(location, 15)
                    barbers.value = barbers.value.map { barber ->
                        barber.apply {
                            distance = getLocation(
                                lat1 = latitude,
                                long1 = longitude,
                                lat2 = barber.lat,
                                long2 = barber.long
                            )
                        }
                    }
                }
                isDialog.value = false
                isDataInitialized.value = true
            }
        }
    }

    private fun getLocation(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val distance: FloatArray =
            FloatArray(1)
        Location.distanceBetween(lat1, long1, lat2, long2, distance)
        var solution = distance[0].toDouble() / 1000
        solution = Math.round(solution * 10.0) / 10.0
        return solution
    }


}