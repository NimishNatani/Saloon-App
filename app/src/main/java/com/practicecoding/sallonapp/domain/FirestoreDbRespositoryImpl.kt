package com.practicecoding.sallonapp.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LastChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.data.model.Message
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.ServiceModel
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreDbRespositoryImpl @Inject constructor(
    @Named("UserData")
    private val usersDb: CollectionReference,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    @Named("BarberData")
    private val barberDb: CollectionReference,
    @ApplicationContext private val context: Context

) : FireStoreDbRepository {
    override suspend fun addUser(userModel: UserModel, imageUri: Uri?): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            CoroutineScope(Dispatchers.IO).launch {
                if (imageUri != null) {
                    val storageRef =
                        storage.reference.child("profile_image/${auth.currentUser?.uid}.jpg")
                    storageRef.putFile(imageUri).addOnCompleteListener { task ->
                        storageRef.downloadUrl.addOnCompleteListener { imageUri ->
                            val downloadImage = imageUri.result
                            userModel.imageUri = downloadImage.toString()
                        }
                    }.await()
                } else {
                    userModel.imageUri =
                        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
                }

                delay(1000)
                usersDb.document(auth.currentUser!!.uid).set(userModel).addOnSuccessListener {
                    trySend(Resource.Success("Successfully Sign In"))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
            }
            awaitClose {
                close()
            }
        }
    override suspend fun updateUserInfo(
        userModel: UserModel,
        imageUri: Uri?
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            val randomInt = (0..10000).random()
            if (imageUri != null) {
                val storageRef = storage.reference.child("profile_image/${auth.currentUser?.uid}$randomInt.jpg")
                try {
                    storageRef.putFile(imageUri).await()
                    val downloadImage = storageRef.downloadUrl.await()
                    userModel.imageUri = downloadImage.toString()
                } catch (e: Exception) {
                    trySend(Resource.Failure(e))
                    close()
                    return@launch
                }
            } else {
                userModel.imageUri = "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
            }
            val updates = mutableMapOf<String, Any?>()

            userModel.name?.takeIf { it.isNotEmpty() }?.let { updates["name"] = it }
            userModel.phoneNumber?.takeIf { it.isNotEmpty() }?.let { updates["phoneNumber"] = it }
            userModel.dateOfBirth?.takeIf { it.isNotEmpty() }?.let { updates["dateOfBirth"] = it }
            userModel.gender?.takeIf { it.isNotEmpty() }?.let { updates["gender"] = it }
            userModel.address?.takeIf { it.isNotEmpty() }?.let { updates["streetAddress"] = it }
            userModel.city?.takeIf { it.isNotEmpty() }?.let { updates["city"] = it }
            userModel.state?.takeIf { it.isNotEmpty() }?.let { updates["state"] = it }
            userModel.imageUri?.takeIf { it.isNotEmpty() }?.let { updates["imageUri"] = it }

            usersDb.document(auth.currentUser?.uid.toString())
                .update(updates)
                .addOnSuccessListener {
                    trySend(Resource.Success("Successfully Updated User Information"))
                }.addOnFailureListener { e ->
                    trySend(Resource.Failure(e))
                }
        }
        awaitClose { close() }
    }

    override suspend fun getUser(): UserModel {
        var userModel = UserModel(
            "User",
            "+91 1111111111",
            "01/01/2000",
            "Gender",
            "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
        )

        suspendCancellableCoroutine<Unit> { continuation ->
            usersDb.document(auth.currentUser!!.uid).get().addOnSuccessListener { documentSnapSot ->
                userModel = documentSnapSot.toObject<UserModel>()!!
                continuation.resume(Unit)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
//        delay(500)
        return userModel
    }

    override suspend fun getBarberPopular(city: String, limit: Long): MutableList<BarberModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.whereEqualTo("city", city).orderBy("rating", Query.Direction.DESCENDING)
                    .limit(limit).get().await()
            val listBarberModel = querySnapshot.documents.map { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("rating") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                    shopStreetAddress = document.getString("shopStreetAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    uid = document.getString("uid").toString(),
                    state = document.getString("state").toString(),
                    city = document.getString("city") ?: "",
                    lat = document.getDouble("lat")!!.toDouble(),
                    long = document.getDouble("long")!!.toDouble(),
                    noOfReviews = document.getString("noOfReviews"),
                    open = document.getBoolean("open")!!,
                    aboutUs = document.getString("aboutUs").toString(),
                    saloonType = document.getString("saloonType").toString()
                )
            }.toMutableList()
//            delay(1000)
            listBarberModel
        }
    }

    override suspend fun getBarberNearby(city: String, limit: Long): MutableList<BarberModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.whereEqualTo("city", city)
                    .limit(limit).get().await()
            val listBarberModel = querySnapshot.documents.map { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("rating") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                    shopStreetAddress = document.getString("shopStreetAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    city = document.getString("city") ?: "",
                    uid = document.getString("uid").toString(),
                    state = document.getString("state").toString(),
                    lat = document.getDouble("lat")!!.toDouble(),
                    long = document.getDouble("long")!!.toDouble(),
                    noOfReviews = document.getString("noOfReviews"),
                    open = document.getBoolean("open")!!,
                    aboutUs = document.getString("aboutUs").toString()
                )
            }.toMutableList()
//            delay(1000)
            listBarberModel
        }
    }

    override suspend fun getBarberByService(service: String): MutableList<BarberModel> {
        return withContext(Dispatchers.IO) {
            val barberList = mutableListOf<BarberModel>()
            val barbersQuerySnapshot = barberDb.get().await()

            for (barberDocument in barbersQuerySnapshot.documents) {
                val servicesQuerySnapshot = barberDb.document(barberDocument.id)
                    .collection("Services")
                    .get()
                    .await()

                for (serviceDocument in servicesQuerySnapshot.documents) {
                    val servicesMap = serviceDocument.data
                    if (servicesMap != null && servicesMap.containsKey(service)) {
                        val barberModel = BarberModel(
                            name = barberDocument.getString("name") ?: "",
                            rating = barberDocument.getDouble("rating") ?: 0.0,
                            shopName = barberDocument.getString("shopName") ?: "",
                            imageUri = barberDocument.getString("imageUri")
                                ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                            shopStreetAddress = barberDocument.getString("shopStreetAddress") ?: "",
                            phoneNumber = barberDocument.getString("phoneNumber") ?: "",
                            uid = barberDocument.getString("uid").toString(),
                            state = barberDocument.getString("state").toString(),
                            city = barberDocument.getString("city") ?: "",
                            lat = barberDocument.getDouble("lat") ?: 0.0,
                            long = barberDocument.getDouble("long") ?: 0.0,
                            noOfReviews = barberDocument.getString("noOfReviews"),
                            open = barberDocument.getBoolean("open") ?: false,
                            aboutUs = barberDocument.getString("aboutUs").toString(),
                            saloonType = barberDocument.getString("saloonType").toString()
                        )
                        barberList.add(barberModel)
                        break
                    }
                }
            }
            barberList.toMutableList()
        }
    }

    override suspend fun getBarber(uid: String?): BarberModel? {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.whereEqualTo("uid", uid)
                    .limit(1).get().await()

            val barberDocument = querySnapshot.documents.firstOrNull()

            barberDocument?.let { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("rating") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                    shopStreetAddress = document.getString("shopStreetAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    city = document.getString("city") ?: "",
                    uid = document.getString("uid").toString(),
                    noOfReviews = document.getString("noOfReviews"),
                    state = document.getString("state").toString(),
                    aboutUs = document.getString("aboutUs").toString(),
                    lat = document.getDouble("lat")!!.toDouble(),
                    long = document.getDouble("long")!!.toDouble(),
                    open = document.getBoolean("open")!!,

                    )
            }
        }
    }

    override suspend fun getServices(uid: String?): MutableList<ServiceCat> {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.document(uid.toString()).collection("Services").get().await()
            val listServiceCat = querySnapshot.documents.map { document ->
                val data = document.data
                val listServiceModel = mutableListOf<ServiceModel>()
                data?.forEach { (serviceName, servicePrice) ->
                    val time = (servicePrice as? Map<*, *>)?.get("serviceDuration") as? String ?: ""
                    val price = (servicePrice as? Map<*, *>)?.get("servicePrice").toString()
                    val serviceModel = ServiceModel(
                        name = serviceName,
                        price = price,
                        time = time
                    )
                    listServiceModel.add(serviceModel)
                }
                ServiceCat(
                    type = document.id,
                    services = listServiceModel
                )
            }.toMutableList()
//            delay(1000)
            listServiceCat
        }
    }

    override suspend fun getTimeSlot(day: String, uid: String): Slots {
        return withContext(Dispatchers.IO) {
            val documentSnapshot =
                barberDb.document(uid).collection("Slots").document(day).get().await()
            val slots = documentSnapshot.let { document ->
                Slots(
                    startTime = document.getString("startTime").toString(),
                    endTime = document.getString("endTime").toString(),
                    booked = document.get("booked") as? List<String> ?: emptyList(),
                    notAvailable = document.get("notAvailable") as? List<String> ?: emptyList(),
                    date = document.getString("date").toString()
                )
            }
            slots
        }
    }

    override suspend fun setBooking(
        barberuid: String,
        useruid: String,
        service: List<Service>,
        gender: List<Int>,
        date: String,
        times: MutableState<List<TimeSlot>>
    ) {
        val currentDate = Date()
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val formattedTime = timeFormat.format(currentTime)
//val selectedService = service.filter { it.count>=1 }
        val bookingData = hashMapOf(
            "barberuid" to barberuid,
            "useruid" to useruid,
            "service" to service,
            "gender" to gender,
            "date" to date,
            "time" to formattedTime,
            "times" to times.value,
            "status" to "pending"
        )
        try {
            Firebase.firestore
                .collection("booking")
                .document(formattedDate)
                .set(bookingData)
                .await()
            Log.d("slotbooking", "Booking successfully set!")
        } catch (e: Exception) {
            Log.d("slotBooking", "Error setting booking: ${e.message}")
        }
    }

    override suspend fun addChat(message: LastMessage, barberuid: String, status:Boolean) {
        try {
            Firebase.firestore.collection("Chats").document("$barberuid${auth.currentUser?.uid}")
                .set(
                    mapOf(
                        "barberuid" to barberuid,
                        "useruid" to auth.currentUser?.uid.toString(),
                        "lastmessage" to message,

                        )
                ).await()
            if(status){
                Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}$barberuid")
                    .collection("Messages").document(message.time).set(message).await()}
        } catch (e: Exception) {
            Log.d("chat", "Error adding chat: ${e.message}")
        }
    }

    override suspend fun getChatUser(): Flow<MutableList<LastChatModel>> = callbackFlow {
        val listenerRegistration = Firebase.firestore.collection("Chats")
            .whereEqualTo("useruid", auth.currentUser?.uid.toString())
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    launch {
                        val chatList = querySnapshot.documents.map { documentSnapshot ->
                            val barberDocument = barberDb.document(documentSnapshot.getString("barberuid").toString()).get().await()
                            val name = barberDocument.getString("name").toString()
                            val image = barberDocument.getString("imageUri").toString()
                            val phoneNumber = barberDocument.getString("phoneNumber").toString()
                            val message = documentSnapshot.get("lastmessage") as Map<*, *>
                            val lastMessage = LastMessage(
                                message = message["message"].toString(),
                                time = message["time"].toString(),
                                status = message["status"].toString().toBoolean(),
                                seenbybarber = message["seenbybarber"].toString().toBoolean(),
                                seenbyuser = message["seenbyuser"].toString().toBoolean()
                            )
                            LastChatModel(
                                name = name,
                                image = image,
                                message = lastMessage,
                                uid = documentSnapshot.getString("barberuid").toString(),
                                phoneNumber = phoneNumber
                            )
                        }.toMutableList()

                        trySend(chatList).isSuccess
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun messageList(barberuid: String): Flow<List<Message>> = callbackFlow {
        val messageRef = Firebase.firestore.collection("Chats")
.document("${auth.currentUser?.uid}$barberuid")
            .collection("Messages")

        val subscription = messageRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                trySend(emptyList()) // Send an empty list on error
                return@addSnapshotListener
            }

            val messageList = querySnapshot?.documents?.map { documentSnapshot ->
                Message(
                    message = documentSnapshot.getString("message").toString(),
                    time = documentSnapshot.getString("time").toString(),
                    status = documentSnapshot.getBoolean("status")!!
                )
            } ?: emptyList()
            trySend(messageList)
        }
        awaitClose { subscription.remove() }
    }
    override suspend fun getOrdersFlow(): Flow<List<OrderModel>> = callbackFlow {
        val listenerRegistration = Firebase.firestore.collection("booking")
            .whereEqualTo("useruid", auth.currentUser?.uid.toString())
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("FireStoreDbRepository", "listen:error", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val orders = mutableListOf<OrderModel>()
                    CoroutineScope(Dispatchers.IO).launch {
                        for (documentSnapshot in snapshots.documents) {
                            val orderId = documentSnapshot.id
                            val serviceNames = mutableListOf<String>()
                            val serviceTypes = mutableListOf<String>()
                            val timesList = mutableListOf<String>()
                            val barberDocument = barberDb
                                .document(documentSnapshot.getString("barberuid").toString()).get().await()
                            val name = barberDocument.getString("name").toString()
                            val shopName = barberDocument.getString("shopName").toString()
                            val image = barberDocument.getString("imageUri").toString()
                            val phoneNo = barberDocument.getString("phoneNumber").toString()
                            val services = documentSnapshot.get("service") as? List<Map<*, *>> ?: emptyList()
                            for (service in services) {
                                serviceNames.add(service["serviceName"].toString())
                                serviceTypes.add(service["type"].toString())
                            }
                            val times = documentSnapshot.get("times") as? List<Map<String, Any>> ?: emptyList()
                            for (time in times) {
                                timesList.add(time["time"].toString())
                            }
                            val orderDate = documentSnapshot.getString("date").toString()
                            val paymentMethod = if (documentSnapshot.contains("paymentMethod")) {
                                documentSnapshot.getString("paymentMethod").toString()
                            } else {
                                "Cash"
                            }
                            val orderStatus = when (documentSnapshot.getString("status").toString().lowercase()) {
                                "completed" -> OrderStatus.COMPLETED
                                "accepted" -> OrderStatus.ACCEPTED
                                "cancelled"-> OrderStatus.CANCELLED
                                else -> OrderStatus.PENDING
                            }
                            val orderModel = OrderModel(
                                imageUrl = image,
                                orderType = serviceNames,
                                timeSlot = timesList,
                                phoneNumber = phoneNo,
                                barberName = name,
                                barberShopName = shopName,
                                paymentMethod = paymentMethod,
                                orderStatus = orderStatus,
                                orderId = orderId,
                                date = orderDate
                            )
                            orders.add(orderModel)
                        }
                        withContext(Dispatchers.Main) {
                            trySend(orders.reversed()).isSuccess
                        }
                    }
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
    override suspend fun updateOrderStatus(orderId: String, status: String):Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            Firebase.firestore.collection("booking").document(orderId)
                .update("status", status)
                .addOnSuccessListener {
                    trySend(Resource.Success("Order status updated successfully"))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
        } catch (e: Exception) {
            trySend(Resource.Failure(e))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun addReview(orderId: String, review: ReviewModel): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading).isSuccess

            // Update review in the order document
            Firebase.firestore.collection("booking").document(orderId)
                .update("review", review)
                .addOnSuccessListener {
                    trySend(Resource.Success("Review added successfully")).isSuccess
                }
                .addOnFailureListener {
                    trySend(Resource.Failure(it)).isSuccess
                }

            // Fetch the order snapshot to update barber's review and rating
            val orderSnapShot = Firebase.firestore.collection("booking").document(orderId)
                .get().await()

            if (orderSnapShot != null) {
                val barberUid = orderSnapShot.getString("barberuid").toString()
                val noOfReviews = orderSnapShot.getLong("noOfReviews")?.toInt() ?: 0
                val rating = orderSnapShot.getDouble("rating") ?: 0.0

                // Calculate new rating
                val newRating = (rating * noOfReviews + review.rating) / (noOfReviews + 1)

                // Update barber's number of reviews and rating
                barberDb.document(barberUid).update("noOfReviews", noOfReviews + 1).await()
                barberDb.document(barberUid).update("rating", newRating).await()
            }
        } catch (e: Exception) {
            trySend(Resource.Failure(e)).isSuccess
        } finally {
            awaitClose { close() }
        }
    }

    override suspend fun getReview(): Flow<Resource<List<ReviewModel>>> = callbackFlow {
        val listenerRegistration = Firebase.firestore.collection("booking")
            .whereEqualTo("useruid", auth.currentUser?.uid.toString())
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("FireStoreDbRepository", "listen:error", e)
                    trySend(Resource.Failure(e))
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val reviews = mutableListOf<ReviewModel>()
                    for (documentSnapshot in snapshots.documents) {
                        val review = documentSnapshot.get("review") as? Map<*, *> ?: emptyMap<String, Any>()
                        val rating = review["rating"] as? Double ?: 0.0
                        val reviewText = review["reviewText"] as? String ?: ""
                        val orderId = documentSnapshot.id
                        val reviewModel = ReviewModel(
                            rating = rating,
                            reviewText = reviewText,
                            orderId = orderId
                        )
                        reviews.add(reviewModel)
                    }
                    trySend(Resource.Success(reviews)).isSuccess
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}