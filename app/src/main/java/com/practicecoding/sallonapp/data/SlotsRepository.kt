package com.practicecoding.sallonapp.data

import com.practicecoding.sallonapp.data.model.Slots

interface SlotsRepository {

    suspend fun getTimeSlot(day:String,uid:String):Slots
}