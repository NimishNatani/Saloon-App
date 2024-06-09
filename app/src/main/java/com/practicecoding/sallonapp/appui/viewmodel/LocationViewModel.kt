package com.practicecoding.sallonapp.appui.viewmodel

import android.app.Application
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.LocationLiveData
import com.practicecoding.sallonapp.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(   repo :LocationRepository ): ViewModel() {

    private val locationLiveData = LocationLiveData(repo.getLocation())
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }
}