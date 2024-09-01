package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val imageUrl: String,
    val orderType: List<String>,
    val timeSlot: List<String>,
    val phoneNumber: String,
    val barberName: String,
    val barberShopName: String,
    val paymentMethod: String? = "Cash",
    var orderStatus: OrderStatus = OrderStatus.PENDING,
    var isCancelRequested: Boolean = false,
    val orderId: String = "",
    val date: String = "",
): Parcelable

data class ReviewModel(
    var rating: Double = 0.0,
    var reviewText: String = "",
    val orderId: String = "",
)

@Parcelize
enum class OrderStatus(val status: String) : Parcelable {
    PENDING("pending"),
    ACCEPTED("accepted"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
}