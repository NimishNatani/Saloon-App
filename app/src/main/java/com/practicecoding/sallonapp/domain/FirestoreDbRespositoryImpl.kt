package com.practicecoding.sallonapp.domain

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRespositoryImpl @Inject constructor(
    private val usersDb:CollectionReference,
    @ApplicationContext private val context: Context

) :FireStoreDbRepository{
    override suspend fun addUser(userModel: UserModel): Flow<Resource<String>> = callbackFlow{
trySend(Resource.Loading)
        val documentId = userModel.Name+userModel.PhoneNumber;
    usersDb.document(documentId)
        .set(userModel)
        .addOnSuccessListener {
            trySend(Resource.Success("Successfully Sign In"))
        }.addOnFailureListener {
            trySend(Resource.Failure(it))
        }
        awaitClose {
            close()
        }
    }
}