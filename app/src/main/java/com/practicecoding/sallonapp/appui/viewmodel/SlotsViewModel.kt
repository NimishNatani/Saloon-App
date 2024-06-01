package com.practicecoding.sallonapp.appui.viewmodel

import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.SlotsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val repo: SlotsRepository
) : ViewModel(){
    suspend fun getSlots(day:String,uid:String) = repo.getTimeSlot(day,uid)
}