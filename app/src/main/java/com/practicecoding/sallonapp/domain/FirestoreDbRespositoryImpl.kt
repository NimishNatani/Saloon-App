package com.practicecoding.sallonapp.domain

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.ServiceModel
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    @OptIn(ExperimentalCoroutinesApi::class)
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

        delay(500)
        return userModel
    }


    override suspend fun getBarberPopular(limit: Long): MutableList<BarberModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.orderBy("rating", Query.Direction.DESCENDING).limit(limit).get().await()
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
                    aboutUs = document.getString("aboutUs").toString()


                )
            }.toMutableList()
            delay(1000)
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
            delay(1000)

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
            delay(1000)
            listServiceCat
        }
    }
}