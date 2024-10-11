package com.practicecoding.sallonapp.data

import android.net.Uri
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.data.model.Message
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.ServiceCategoryModel
import com.practicecoding.sallonapp.data.model.Slots
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

    suspend fun getBarberPopular(city: String, limit: Long): MutableList<BarberModel>
    suspend fun getBarberNearby(city: String, limit: Long): MutableList<BarberModel>

    suspend fun getAllCityBarber(city: String):Flow<MutableList<BarberModel>>

    suspend fun getBarberByService(service: String): MutableList<BarberModel>

    suspend fun getBarber(uid: String?): BarberModel?
    suspend fun getServices(uid: String?): MutableList<ServiceCategoryModel>

    suspend fun getTimeSlot(day: String, uid: String): Slots

    suspend fun setBooking(
        bookingModel: BookingModel
    )

    suspend fun addChat(message: LastMessage, barberUid: String, status: Boolean)
    suspend fun getChatUser(): Flow<MutableList<ChatModel>>
    suspend fun messageList(barberUid: String): Flow<MutableList<Message>>
    suspend fun getOrder(): Flow<List<OrderModel>>
    suspend fun updateOrderStatus(order: OrderModel, status: String): Flow<Resource<String>>
    suspend fun addReview(order: OrderModel, review: ReviewModel): Flow<Resource<String>>
    suspend fun getReview(barberUid: String): Flow<Resource<MutableList<ReviewModel>>>
}