package com.practicecoding.sallonapp.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookedModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.data.model.Message
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.ServiceCategoryModel
import com.practicecoding.sallonapp.data.model.ServiceModel
import com.practicecoding.sallonapp.data.model.Slots
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
                val storageRef =
                    storage.reference.child("profile_image/${auth.currentUser?.uid}$randomInt.jpg")
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
                userModel.imageUri =
                    "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
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

    override suspend fun getAllCityBarber(city: String): Flow<Resource<MutableList<BarberModel>>> =
        callbackFlow {

            val listenerRegistration = barberDb.whereEqualTo("city", city)
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        launch {

                            val listOfBarber = querySnapshot.documents.map { document ->
                                BarberModel(
                                    name = document.getString("name") ?: "",
                                    rating = document.getDouble("rating") ?: 0.0,
                                    shopName = document.getString("shopName") ?: "",
                                    imageUri = document.getString("imageUri")
                                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                                    shopStreetAddress = document.getString("shopStreetAddress")
                                        ?: "",
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

                            trySend(Resource.Success(listOfBarber.toMutableList())).isSuccess
                        }
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }.flowOn(Dispatchers.IO)

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
                    aboutUs = document.getString("aboutUs").toString(),
                    saloonType = document.getString("saloonType").toString()
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

    override suspend fun getServices(uid: String?): MutableList<ServiceCategoryModel> {
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
                ServiceCategoryModel(
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
        bookingModel: BookingModel
    ) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val querySnapshot =
            Firebase.firestore.collection("users").document(auth.currentUser?.uid.toString()).get()
                .await()
        val userDp = querySnapshot.getString("imageUri").toString()
        val userName = querySnapshot.getString("name").toString()

        val bookingData = hashMapOf(
            "barberuid" to bookingModel.barber.uid,
            "useruid" to auth.currentUser?.uid,
            "listOfService" to bookingModel.listOfService,
            "genderCounter" to bookingModel.genderCounter,
            "selectedDate" to bookingModel.selectedDate.format(formatter),
            "selectedSlots" to bookingModel.selectedSlots,
            "dateandtime" to formattedDate,
            "status" to "pending",
            "paymentMethod" to "Cash",
            "review" to ReviewModel(userDp = userDp, userName = userName)
        )
        try {
            Firebase.firestore
                .collection("Booking")
                .document(bookingModel.barber.uid + auth.currentUser?.uid)
                .set(
                    mapOf(
                        "barberuid" to bookingModel.barber.uid,
                        "useruid" to auth.currentUser?.uid
                    )
                )
                .await()
            Firebase.firestore.collection("Booking")
                .document(bookingModel.barber.uid + auth.currentUser?.uid)
                .collection("Order").document(formattedDate).set(bookingData).await()
        } catch (e: Exception) {
            Log.d("slotBooking", "Error setting booking: ${e.message}")
        }
    }

    override suspend fun addChat(message: LastMessage, barberuid: String, status: Boolean) {
        try {
            Firebase.firestore.collection("Chats").document("$barberuid${auth.currentUser?.uid}")
                .set(
                    mapOf(
                        "barberuid" to barberuid,
                        "useruid" to auth.currentUser?.uid.toString(),
                        "lastmessage" to message,

                        )
                ).await()
            if (status) {
                Firebase.firestore.collection("Chats")
                    .document("${auth.currentUser?.uid}$barberuid")
                    .collection("Messages").document(message.time).set(message).await()
            }
        } catch (e: Exception) {
            Log.d("chat", "Error adding chat: ${e.message}")
        }
    }

    override suspend fun getChatUser(): Flow<MutableList<ChatModel>> = callbackFlow {
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
                            val barberDocument = barberDb.document(
                                documentSnapshot.getString("barberuid").toString()
                            ).get().await()
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
                            ChatModel(
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

    override suspend fun messageList(barberuid: String): Flow<MutableList<Message>> = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -14) // Subtract 14 days from today
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val fourteenDaysAgo = calendar.time

        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val messageRef = Firebase.firestore.collection("Chats")
            .document("${auth.currentUser?.uid}$barberuid")
            .collection("Messages")

        val subscription =
            messageRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    trySend(emptyList<Message>().toMutableList()) // Send an empty list on error
                    return@addSnapshotListener
                }

                val messageList = querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                    val timeString = documentSnapshot.get("time").toString()
                    val orderDate = timeString.let { dateFormat.parse(it) }

                    if (orderDate != null && orderDate.after(fourteenDaysAgo)) {
                        Message(
                            message = documentSnapshot.getString("message").orEmpty(),
                            time = timeString,
                            status = documentSnapshot.getBoolean("status") ?: false
                        )
                    } else if (orderDate != null) {
                        documentSnapshot.reference.delete()
                        null
                    } else {
                        null // Skip messages older than 14 days or with invalid data
                    }
                }?.toMutableList() ?: mutableListOf()

                trySend(messageList)
            }

        awaitClose { subscription.remove() }
    }.flowOn(Dispatchers.IO)


    override suspend fun getOrder(): Flow<List<OrderModel>> = callbackFlow {
        val db = FirebaseFirestore.getInstance()

        // Calculate the date 8 days ago
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -8) // Subtract 8 days from today
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val eightDaysAgo = calendar.time

        // Date format that matches the Firestore 'time' field format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


        val listenerRegistrations = mutableListOf<ListenerRegistration>()

        // Step 1: Query Bookings where userUid matches
        db.collection("Booking")
            .whereEqualTo("useruid", auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { bookingsSnapshot ->
                // Step 2: Set listeners on each order subcollection of the matching bookings
                for (booking in bookingsSnapshot.documents) {
                    val listenerRegistration = booking.reference.collection("Order")
                        .addSnapshotListener { ordersSnapshot, error ->
                            if (error != null) {
                                close(error) // If there is an error, close the flow with the error
                            } else if (ordersSnapshot != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    // Collect orders data from the snapshot and process them in parallel
                                    val jobs = ordersSnapshot.documents.map { order ->
                                        async {
                                            val timeString = order.get("selectedDate").toString()

                                            val orderDate = timeString.let {
                                                dateFormat.parse(it)
                                            }

                                            if (orderDate != null && orderDate.after(eightDaysAgo)) {
                                                // Convert Firestore document to BookedModel
                                                val bookedModel =
                                                    order.toObject(BookedModel::class.java)
                                                // Fetch barber details asynchronously
                                                val barberDocument = bookedModel?.let {
                                                    barberDb.document(it.barberuid).get().await()
                                                }

                                                val name =
                                                    barberDocument?.getString("name").toString()
                                                val shopName =
                                                    barberDocument?.getString("shopName").toString()
                                                val image =
                                                    barberDocument?.getString("imageUri").toString()
                                                val phoneNo =
                                                    barberDocument?.getString("phoneNumber")
                                                        .toString()
                                                val listOfService =
                                                    bookedModel?.listOfService ?: listOf()
                                                val timeSlots =
                                                    bookedModel?.selectedSlots ?: listOf()
                                                val review = bookedModel?.review as ReviewModel

                                                val orderStatus = when (bookedModel.status) {
                                                    "completed" -> OrderStatus.COMPLETED
                                                    "accepted" -> OrderStatus.ACCEPTED
                                                    "cancelled" -> OrderStatus.CANCELLED
                                                    else -> OrderStatus.PENDING
                                                }

                                                // Create OrderModel and add it to the list
                                                val orderModel =
                                                    OrderModel(
                                                        imageUrl = image,
                                                        listOfService = listOfService,
                                                        timeSlot = timeSlots,
                                                        phoneNumber = phoneNo,
                                                        barberName = name,
                                                        barberShopName = shopName,
                                                        paymentMethod = "Cash",
                                                        orderStatus = orderStatus,
                                                        orderId = bookedModel.dateandtime,
                                                        date = bookedModel.selectedDate,
                                                        barberuid = bookedModel.barberuid,
                                                        useruid = bookedModel.useruid,
                                                        review = review,
                                                        genderCounter = bookedModel.genderCounter
                                                    )


                                                orderModel
                                            } else {

                                                if (order.getString("status") == "cancelled" || order.getString(
                                                        "status"
                                                    ) == "pending" || order.getString("status") == "accepted"
                                                ) {
                                                    order.reference.delete()
                                                }
                                                null
                                            }
                                        }
                                    }

                                    // Wait for all the jobs to complete
                                    val results = jobs.awaitAll().filterNotNull()

                                    // Sort the orders by time in descending order
                                    val sortedOrders = results.sortedByDescending {
                                        it.orderId
                                    }

                                    // Emit the sorted list of orders
                                    trySend(sortedOrders).isSuccess
                                }
                            }
                        }

                    // Store the listener to remove later
                    listenerRegistrations.add(listenerRegistration)
                }
            }
            .addOnFailureListener { exception ->
                close(exception) // Close the flow with the error
            }

        // Await close of the flow
        awaitClose {
            listenerRegistrations.forEach { it.remove() } // Clean up listeners when the flow is closed
        }
    }

    override suspend fun updateOrderStatus(order: OrderModel, status: String)
            : Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            Firebase.firestore.collection("Booking").document(order.barberuid + order.useruid)
                .collection("Order").document(order.orderId)
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

    override suspend fun addReview(order: OrderModel, review: ReviewModel): Flow<Resource<String>> =
        callbackFlow {
            try {
                trySend(Resource.Loading)
                // Update review in the order document
                Firebase.firestore.collection("Booking").document(order.barberuid + order.useruid)
                    .collection("Order").document(order.orderId)
                    .update("review", review)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Review added successfully")).isSuccess
                    }
                    .addOnFailureListener {
                        trySend(Resource.Failure(it)).isSuccess
                    }
                val barberSnapshot = barberDb.document(order.barberuid).get().await()
                var rating = barberSnapshot.get("rating").toString().toDouble()
                var noOfReview = barberSnapshot.get("noOfReviews").toString().toInt()

//                rating = ((rating*noOfReview)+review.rating)/(++noOfReview)
                // Update barber's number of reviews and rating
                barberDb.document(order.barberuid).update("noOfReviews", (++noOfReview).toString())
                    .await()
                barberDb.document(order.barberuid)
                    .update("rating", ((rating * (noOfReview - 1)) + review.rating) / (noOfReview))
                    .await()

            } catch (e: Exception) {
                trySend(Resource.Failure(e)).isSuccess
            } finally {
                awaitClose { close() }
            }
        }

    override suspend fun getReview(barberuid: String): Flow<Resource<MutableList<ReviewModel>>> =
        callbackFlow {
            val db = FirebaseFirestore.getInstance()
            val listenerRegistrations = mutableListOf<ListenerRegistration>()

            // Step 1: Query Bookings where userUid matches
            db.collection("Booking")
                .whereEqualTo("barberuid", barberuid)
                .get()
                .addOnSuccessListener { bookingsSnapshot ->
                    // Step 2: Set listeners on each order subcollection of the matching bookings
                    for (booking in bookingsSnapshot.documents) {
                        val listenerRegistration = booking.reference.collection("Order")
                            .addSnapshotListener { ordersSnapshot, error ->
                                if (error != null) {
                                    close(error) // If there is an error, close the flow with the error
                                } else if (ordersSnapshot != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        // Collect orders data from the snapshot and process them in parallel
                                        val jobs = ordersSnapshot.documents.map { order ->
                                            async {
                                                // Convert Firestore document to BookedModel
                                                val bookedModel =
                                                    order.toObject(BookedModel::class.java)
                                                if (bookedModel != null && bookedModel.review.rating.toString()
                                                        .isNotEmpty() && bookedModel.review.reviewTime.isNotEmpty()
                                                ) {
                                                    val reviewModel =
                                                        ReviewModel(
                                                            rating = bookedModel.review.rating,
                                                            reviewTime = bookedModel.review.reviewTime,
                                                            reviewText = bookedModel.review.reviewText,
                                                            userDp = bookedModel.review.userDp,
                                                            userName = bookedModel.review.userName
                                                        )
                                                    reviewModel
                                                } else {
                                                    null
                                                }
                                            }

                                        }

                                        // Wait for all the jobs to complete
                                        val results = jobs.awaitAll().filterNotNull()

                                        // Sort the orders by time in descending order
                                        val sortedOrders = results.sortedByDescending {
                                            it.reviewTime
                                        }

                                        // Emit the sorted list of orders
                                        trySend(Resource.Success(sortedOrders.toMutableList())).isSuccess
                                    }
                                }
                            }

                        // Store the listener to remove later
                        listenerRegistrations.add(listenerRegistration)
                    }
                }
                .addOnFailureListener { exception ->
                    close(exception) // Close the flow with the error
                }

            // Await close of the flow
            awaitClose {
                listenerRegistrations.forEach { it.remove() } // Clean up listeners when the flow is closed
            }
        }
}