package com.practicecoding.sallonapp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

//    @Provides
//    @Singleton
//    fun providesRealtimeDb():DatabaseReference =
//        Firebase.database.reference.child("user")

    @Singleton
    @Provides
    fun providesFirestoreDb(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    @Named("UserData")
    fun provideUserData(): CollectionReference = Firebase.firestore.collection("users")

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun storageRefrence(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    @Named("BarberData")
    fun provideBarberData():CollectionReference = Firebase.firestore.collection("barber")

}