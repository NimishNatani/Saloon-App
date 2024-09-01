package com.practicecoding.sallonapp.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LastChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.data.model.Message
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FireStoreDbRepository {

    suspend fun addUser(
        userModel: UserModel,
        imageUri: Uri?
    ): Flow<Resource<String>>

    suspend fun updateUserInfo(
        userModel: UserModel,
        imageUri: Uri?
    ): Flow<Resource<String>>

    suspend fun getUser(
    ): UserModel?

    suspend fun getBarberPopular(city: String,limit:Long):MutableList<BarberModel>
    suspend fun getBarberNearby(city:String,limit:Long):MutableList<BarberModel>

    suspend fun getBarberByService(service:String):MutableList<BarberModel>

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
    suspend fun addChat(message: LastMessage, barberUid: String, status:Boolean)
    suspend fun getChatUser():Flow<MutableList<LastChatModel>>
    suspend fun messageList(barberUid: String):Flow<List<Message>>
    suspend fun getOrdersFlow(): Flow<List<OrderModel>>
    suspend fun updateOrderStatus(orderId: String, status: String): Flow<Resource<String>>
    suspend fun addReview(orderId: String, review:ReviewModel): Flow<Resource<String>>
    suspend fun getReview(): Flow<Resource<List<ReviewModel>>>
}