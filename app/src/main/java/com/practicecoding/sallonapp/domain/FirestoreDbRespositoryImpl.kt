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
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.Message
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
                    StartTime = document.getString("startTime").toString(),
                    EndTime = document.getString("endTime").toString(),
                    Booked = document.get("booked") as? List<String> ?: emptyList(),
                    NotAvailable = document.get("notAvailable") as? List<String> ?: emptyList(),
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
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        val bookingData = hashMapOf(
            "barberuid" to barberuid,
            "useruid" to useruid,
            "service" to service,
            "gender" to gender,
            "date" to date,
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
    override suspend fun addChat(message: Message, barberUid: String) {
        try {
            Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}+$barberUid")
                .set(
                    mapOf(
                        "barberuid" to barberUid,
                        "useruid" to auth.currentUser?.uid.toString(),
                        "lastmessage" to message
                    )
                ).await()
            Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}+$barberUid")
                .collection("Messages").document(message.time).set(message).await()
        } catch (e: Exception) {
            Log.d("chat", "Error adding chat: ${e.message}")
        }
    }

    override suspend fun getChatUser(): MutableList<ChatModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = Firebase.firestore.collection("Chats")
                .whereEqualTo("useruid", auth.currentUser?.uid.toString()).get().await()
            val chatList = querySnapshot.documents.map { documentSnapshot ->
                val barberDocument =
                    barberDb.document(documentSnapshot.getString("barberuid").toString()).get()
                        .await()
                val name = barberDocument.getString("name").toString()
                val image = barberDocument.getString("imageUri")
                    .toString() // Assuming "image" field contains the URL of the image
                val message = documentSnapshot.get("lastmessage") as Map<*, *>
                val lastMessage = Message(
                    message = message["message"].toString(),
                    time = message["time"].toString(),
                    status = message["status"].toString().toBoolean()
                )
                ChatModel(
                    name = name,
                    image = image, // Add the image URL to ChatModel
                    message = lastMessage,
                    uid=documentSnapshot.getString("barberuid").toString()
                )
            }.toMutableList()
            Log.d("userchat", "$chatList")
            chatList
        }
    }

    override suspend fun messageList(barberUid: String): Flow<List<Message>> = callbackFlow {
        val messageRef = Firebase.firestore.collection("Chats")
            .document("${auth.currentUser?.uid}+$barberUid")
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
}