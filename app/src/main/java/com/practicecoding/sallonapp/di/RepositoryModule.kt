package com.practicecoding.sallonapp.di

import com.practicecoding.sallonapp.data.AuthRepository
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.LocationRepository
import com.practicecoding.sallonapp.domain.AuthRepositoryImpl
import com.practicecoding.sallonapp.domain.FirestoreDbRespositoryImpl
import com.practicecoding.sallonapp.domain.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesFirestoreRepository(
        repo:FirestoreDbRespositoryImpl
    ):FireStoreDbRepository

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun providesLocation(
        repo:LocationRepositoryImpl
    ):LocationRepository

}