package com.practicecoding.sallonapp.domain

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
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
import javax.inject.Inject

class FirestoreDbRespositoryImpl @Inject constructor(
    private val usersDb:CollectionReference,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context

) :FireStoreDbRepository{
    override suspend fun addUser(userModel: UserModel, imageUri: Uri?): Flow<Resource<String>> =
        callbackFlow {
           trySend(Resource.Loading)
            val documentId = userModel.Name + userModel.PhoneNumber
            CoroutineScope(Dispatchers.IO).launch{
                if (imageUri != null) {
                    val storageRef =
                        storage.reference.child("profile_image/${auth.currentUser?.uid}.jpg")
                    storageRef.putFile(imageUri).addOnCompleteListener { task ->
                        storageRef.downloadUrl.addOnCompleteListener { imageUri ->
                            val downloadImage = imageUri.result
                            userModel.ImageUri = downloadImage.toString()
                        }
                    }.await()
                } else {
                    userModel.ImageUri =
                        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
                }

                delay(1000)
                usersDb.document(documentId)
                    .set(userModel)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Successfully Sign In"))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }
            }
            awaitClose {
                close()
            }

}
}