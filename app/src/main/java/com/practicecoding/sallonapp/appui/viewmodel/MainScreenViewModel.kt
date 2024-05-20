package com.practicecoding.sallonapp.appui.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel : ViewModel() {
    var barberPopularModel = mutableStateOf<List<BarberModel>>(emptyList())
    var barberNearbyModel = mutableStateOf<List<BarberModel>>(emptyList())
    var isDialog = mutableStateOf(true)
    var isDialog2 = mutableStateOf(true)
    var locationDetails = mutableStateOf(LocationModel(null, null, null, null, null))
    var isDataInitialized = mutableStateOf(false)
    var isDataInitialized2 = mutableStateOf(false)
    var userModel = mutableStateOf(UserModel())

    fun initializeData(viewModelBarber: GetBarberDataViewModel, locationDetails: LocationModel) {
        if (isDataInitialized.value) return

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isDialog.value = true
                delay(500)
                if (locationDetails.city != null) {
                    val nearbyBarbers =
                        viewModelBarber.getBarberNearby(locationDetails.city.toString(), 6)
                    barberNearbyModel.value = nearbyBarbers.map { barber ->
                        barber.apply {
                            distance = getLocation(
                                lat1 = locationDetails.latitude!!.toDouble(),
                                long1 = locationDetails.longitude!!.toDouble(),
                                lat2 = barber.lat,
                                long2 = barber.long
                            )
                        }
                    }.sortedBy { it.distance }
                }
                barberPopularModel.value =
                    viewModelBarber.getBarberPopular(locationDetails.city.toString(), 6)
                barberPopularModel.value = barberPopularModel.value.map { barber ->
                    barber.apply {
                        distance = getLocation(
                            lat1 = locationDetails.latitude!!.toDouble(),
                            long1 = locationDetails.longitude!!.toDouble(),
                            lat2 = barber.lat,
                            long2 = barber.long
                        )
                    }
                }
                isDialog.value = false
                isDataInitialized.value = true

            }
        }
    }

    fun initializedUser(userDataViewModel: GetUserDataViewModel) {
        if (isDataInitialized2.value) return

        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isDialog2.value = true
                delay(500)
                userModel.value = userDataViewModel.getUser()!!
                isDialog2.value = false
                isDataInitialized2.value = true

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
