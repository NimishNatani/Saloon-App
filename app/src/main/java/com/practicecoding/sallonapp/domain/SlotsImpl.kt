package com.practicecoding.sallonapp.domain

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.SlotsRepository
import com.practicecoding.sallonapp.data.model.Slots
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SlotsImpl@Inject constructor(
    @Named("UserData")
    private val usersDb: CollectionReference,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    @Named("BarberData")
    private val barberDb: CollectionReference,
    @ApplicationContext private val context: Context

) : SlotsRepository {
    override suspend fun getTimeSlot(day: String,uid:String): Slots {
        return withContext(Dispatchers.IO){
            val documentSnapshot = barberDb.document(uid).collection("Slots").document(day).get().await()
            val slots = documentSnapshot.let { document->
                Slots(
                    StartTime = document.getString("StartTime").toString(),
                    EndTime = document.getString("EndTime").toString(),
                    Booked = document.get("Booked") as? List<String>?: emptyList(),
                    NotAvailable = document.get("NotAvailable") as? List<String>?: emptyList(),
                    date = document.getString("date").toString()
                )
            }
            slots
        }
    }
}