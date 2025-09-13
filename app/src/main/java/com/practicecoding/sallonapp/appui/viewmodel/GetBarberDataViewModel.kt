package com.practicecoding.sallonapp.appui.viewmodel

import android.location.Location
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCategoryModel
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.data.model.locationObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private var _barberPopular = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    var barberPopular: StateFlow<MutableList<BarberModel>> = _barberPopular.asStateFlow()

    private var _barberNearby = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    var barberNearby: StateFlow<MutableList<BarberModel>> = _barberNearby.asStateFlow()

    private var _likedBarberList = mutableStateOf<List<BarberModel>>(emptyList())
    val likedBarberList: State<List<BarberModel>> = _likedBarberList

    private var _services = MutableStateFlow(emptyList<ServiceCategoryModel>().toMutableList())
    var services: StateFlow<MutableList<ServiceCategoryModel>> = _services.asStateFlow()

    var genderCounter = mutableStateOf(value = listOf(0, 0, 0))

    private var _listOfService = MutableStateFlow(emptyList<Service>().toMutableList())
    var listOfService: StateFlow<MutableList<Service>> = _listOfService.asStateFlow()

    private var _slots = MutableStateFlow(Slots("08:00", "22:00"))
    var slots: StateFlow<Slots> = _slots.asStateFlow()

    var selectedSlots = mutableStateListOf<TimeSlot>()

    private val _barberList = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    val barberList: StateFlow<MutableList<BarberModel>> = _barberList.asStateFlow()

    private val _barberListByState = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    val barberListByState : StateFlow<MutableList<BarberModel>> = _barberListByState.asStateFlow()

    private val _barberListByService = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    val barberListByService: StateFlow<MutableList<BarberModel>> = _barberListByService.asStateFlow()

    private val _barberReviewList = MutableStateFlow(emptyList<ReviewModel>().toMutableList())
    val barberReviewList: StateFlow<MutableList<ReviewModel>> = _barberReviewList.asStateFlow()

    private val _offerCard = MutableStateFlow(emptyList<BarberModel>().toMutableList())
    val offerCard:StateFlow<MutableList<BarberModel>> = _offerCard.asStateFlow()

    var navigationItem = mutableStateOf(NavigationItem.Home)

    var showDialog = mutableStateOf(false)
    var dialogMessage = mutableStateOf(listOf("", ""))
    var shimmerVisible = mutableStateOf(true)


    suspend fun getBarberListByService(service: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _barberListByService.emit(repo.getBarberByService(service))
            _barberListByService.value.map { barber ->
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

    suspend fun getAllCityBarber(city: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.getAllCityBarber(city).collect {
                _barberList.emit(it)
                Log.d("barber", _barberList.toString())
                _barberList.value.map { barber ->
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
                _barberPopular.emit(
                    _barberList.value.sortedByDescending { it.rating }.take(6).toMutableList()
                )
                _barberNearby.emit(
                    _barberList.value.sortedBy { it.distance }.take(6).toMutableList()
                )
            }
        }
    }

    suspend fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.getBarberNearby -> getBarberNearby(event.city, event.limit)
            is MainEvent.getBarberPopular -> getBarberPopular(event.city, event.limit)
            is MainEvent.getServices -> getServices(event.uid)
            is MainEvent.getSlots -> getSlots(event.day, event.uid)
            is MainEvent.setBooking -> setBooking(
                event.bookingModel
            )

            else -> {}
        }
    }

    private suspend fun getBarberPopular(city: String, limit: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _barberPopular.emit(repo.getBarberPopular(city, limit))
            _barberPopular.emit(barberPopular.value.map { barber ->
                barber.apply {
                    val locationDetails = locationObject.locationDetails
                    distance = getLocation(
                        lat1 = locationDetails.latitude!!.toDouble(),
                        long1 = locationDetails.longitude!!.toDouble(),
                        lat2 = barber.lat,
                        long2 = barber.long
                    )
                }
            }.toMutableList())
        }
    }

    private suspend fun getBarberNearby(city: String, limit: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _barberNearby.emit(repo.getBarberNearby(city, limit))
            _barberNearby.emit(barberNearby.value.map { barber ->
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
                .sortedBy { it.distance }
                .toMutableList())
        }
    }
    suspend fun getBarberListByState(state:String){
        viewModelScope.launch(Dispatchers.Default) {
            repo.getAllStateBarber(state).collect {
                _barberListByState.emit(it)
                _barberListByState.value.map { barber ->
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
    }

    private suspend fun getBarber(uid: String?): BarberModel? = repo.getBarber(uid)

    suspend fun getBarberListById(uidList: List<String>) {
        viewModelScope.launch {
            val barbers = uidList.mapNotNull { uid ->
                getBarber(uid)
            }
            _likedBarberList.value = barbers
        }
    }

    private suspend fun getServices(uid: String?) {
        viewModelScope.launch { _services.emit(repo.getServices(uid)) }
    }

    private fun getLocation(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val distance: FloatArray =
            FloatArray(1)
        Location.distanceBetween(lat1, long1, lat2, long2, distance)
        var solution = distance[0].toDouble() / 1000
        solution = Math.round(solution * 10.0) / 10.0
        return solution
    }

    suspend fun getReviewList(barberuid: String) {
        repo.getReview(barberuid).collect {

                    _barberReviewList.emit(it.toMutableList())
                    _barberReviewList.value.distinct()
                    Log.d("ReviewModel", "getReviewList: $it")
        }
    }

    private suspend fun getSlots(day: String, uid: String) {
        viewModelScope.launch { _slots.emit(repo.getTimeSlot(day, uid)) }
    }

    fun initializedServices(serviceCat: List<ServiceCategoryModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            if (_listOfService.value.isEmpty()) {
                _listOfService.emit(serviceCat.flatMap { servicecat ->
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
                }.toMutableList())
            }
        }
    }

    fun updateService(updatedService: Service) {
        viewModelScope.launch {
            _listOfService.emit(_listOfService.value.map {
                if (it.id == updatedService.id) updatedService else it
            }.toMutableList())
        }
    }

    suspend fun setBooking(
        bookingModel: BookingModel
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setBooking(bookingModel)
        }
    }

}

sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent()
    data class getServices(val uid: String) : MainEvent()
    data class getSlots(val day: String, val uid: String) : MainEvent()
    data class setBooking(
        val bookingModel: BookingModel
    ) : MainEvent()
}