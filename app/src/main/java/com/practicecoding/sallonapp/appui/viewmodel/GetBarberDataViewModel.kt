package com.practicecoding.sallonapp.appui.viewmodel

import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel(){

    suspend fun getBarberPopular() = repo.getBarberPopular()
    suspend fun getBarberNearby(city:String) = repo.getBarberNearby(city)
}