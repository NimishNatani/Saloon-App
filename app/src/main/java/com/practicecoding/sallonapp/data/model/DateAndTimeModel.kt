package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import com.practicecoding.sallonapp.appui.screens.MainScreens.SlotStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TimeSlot( val time:  String, val date: String, val status: SlotStatus) : Parcelable

@Parcelize
data class DateSlots(val date: LocalDate, val slots: List<TimeSlot>) : Parcelable

data class Slots(
    val StartTime: String,
    val EndTime: String,
    val Booked: List<String>? = emptyList(),
    val NotAvailable: List<String>? = emptyList(),
    val date: String= LocalDate.now().toString()
)