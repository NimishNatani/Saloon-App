package com.practicecoding.sallonapp.data

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.MutableState
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.ServiceModel
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FireStoreDbRepository {

    suspend fun addUser(
        userModel: UserModel,
        imageUri: Uri?
    ): Flow<Resource<String>>

    suspend fun getUser(
    ): UserModel?

    suspend fun getBarberPopular(city: String,limit:Long):MutableList<BarberModel>
    suspend fun getBarberNearby(city:String,limit:Long):MutableList<BarberModel>

    suspend fun getBarber(uid:String?):BarberModel?
    suspend fun getServices(uid:String?):MutableList<ServiceCat>

    suspend fun getTimeSlot(day:String,uid:String): Slots

    suspend fun setBooking(barberuid: String,
                           useruid: String,
                           service: List<Service>,
                           gender: List<Int>,
                           date: String,
                           times: MutableState<List<TimeSlot>>
    )



}