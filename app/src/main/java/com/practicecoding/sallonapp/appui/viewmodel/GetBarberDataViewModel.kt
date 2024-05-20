package com.practicecoding.sallonapp.appui.viewmodel

import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel(){

    suspend fun getBarberPopular(city: String,limit:Long) = repo.getBarberPopular(city,limit)
    suspend fun getBarberNearby(city:String,limit:Long) = repo.getBarberNearby(city,limit)

    suspend fun getBarber(uid:String?) = repo.getBarber(uid)
    suspend fun getServices(uid:String?) = repo.getServices(uid)
}