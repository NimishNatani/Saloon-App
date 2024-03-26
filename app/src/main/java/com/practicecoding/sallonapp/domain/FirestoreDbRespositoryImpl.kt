package com.practicecoding.sallonapp.domain

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.Query
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

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
        CoroutineScope(Dispatchers.IO).launch {

            usersDb.document(auth.currentUser!!.uid).get().addOnSuccessListener { documentSnapSot ->
                userModel = documentSnapSot.toObject<UserModel>()!!
            }.addOnFailureListener {
                Toast.makeText(context, "Error in Loading the Data", Toast.LENGTH_SHORT).show()
            }
        }
        delay(1000)
        return userModel
    }

    override suspend fun getBarberPopular(): MutableList<BarberModel> {

        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.orderBy("review", Query.Direction.DESCENDING).limit(4).get().await()
            val listBarberModel = querySnapshot.documents.map { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("review") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                    shopStreetAddress = document.getString("shopAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",


                    // Map other fields as needed
                )
            }.toMutableList()
            delay(2000)

            listBarberModel
        }

    }

    override suspend fun getBarberNearby(city:String): MutableList<BarberModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.whereEqualTo("city", city)
                    .limit(4).get().await()
            val listBarberModel = querySnapshot.documents.map { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("review") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
                    shopStreetAddress = document.getString("shopAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    city = document.getString("city")?:""


                    // Map other fields as needed
                )
            }.toMutableList()
            delay(1000)

            listBarberModel
        }
    }
}