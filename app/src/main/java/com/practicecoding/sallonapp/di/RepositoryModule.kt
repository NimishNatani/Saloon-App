package com.practicecoding.sallonapp.di

import com.practicecoding.sallonapp.data.AuthRepository
import com.practicecoding.sallonapp.domain.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

//    @Binds
//    abstract fun providesRealtimeRepository(
//        repo:RealtimeDbRepository
//    ):ReatimeRepository
//
//    @Binds
//    abstract fun providesFirestoreRepository(
//        repo:FirestoreDbRepositoryImpl
//    ):FirestoreRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository

}