package com.practicecoding.sallonapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Parcelize
data class OrderModel(
    val barberuid: String="",
    val useruid: String="",
    val imageUrl: String,
    val listOfService:  List<Service>,
    val timeSlot: List<TimeSlot>,
    val phoneNumber: String,
    val barberName: String,
    val barberShopName: String,
    val paymentMethod: String? = "Cash",
    var orderStatus: OrderStatus = OrderStatus.PENDING,
    var isCancelRequested: Boolean = false,
    val orderId: String = "",
    val date: String = "",
    var review:ReviewModel = ReviewModel(),
    val genderCounter: List<Int> =listOf()
) : Parcelable

@Serializable()
@Parcelize
data class BookingModel(
    var barber: BarberModel = BarberModel(),
    var services: MutableList<ServiceCategoryModel> = emptyList<ServiceCategoryModel>().toMutableList(),
    var genderCounter: List<Int> = listOf(0, 0, 0),
    var listOfService: List<Service> = emptyList(),
    var selectedSlots: List<TimeSlot> = emptyList(),
    var selectedDate: @Contextual LocalDate = LocalDate.now(),
//    var reviewList:MutableList<OrderModel> = emptyList<OrderModel>().toMutableList()

) : Parcelable

@Serializable
@Parcelize
data class BookedModel(
    var barberuid: String = "",
    var genderCounter: List<Int> = listOf(0, 0, 0),
    var listOfService: List<Service> = emptyList(),
    var selectedSlots: List<TimeSlot> = emptyList(),
    var selectedDate: String = "",
    var status: String = "pending",
    var useruid: String = "",
    var dateandtime:String="",
    var paymentMethod: String = "Cash",
    var review:ReviewModel = ReviewModel()
) : Parcelable
@Serializable
@Parcelize
data class ReviewModel(
    var rating: Double = 0.0,
    var reviewText: String = "",
    val userDp: String = "",
    val userName: String = "",
    val reviewTime:String = ""
):Parcelable

//@OptIn(ExperimentalSerializationApi::class)
//@Serializer(forClass = LocalDate::class)
//object DateSerializer :KSerializer<LocalDate>{
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//    override fun serialize(encoder: Encoder, value: LocalDate) {
//        encoder.encodeString(value.format(formatter))
//    }
//
//    override fun deserialize(decoder: Decoder): LocalDate {
//        return LocalDate.parse(decoder.decodeString(), formatter)
//    }
//}
@Parcelize
enum class OrderStatus(val status: String) : Parcelable {
    PENDING("pending"),
    ACCEPTED("accepted"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
}