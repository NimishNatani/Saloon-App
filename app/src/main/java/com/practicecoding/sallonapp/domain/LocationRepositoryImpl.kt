package com.practicecoding.sallonapp.domain

import android.content.Context
import com.practicecoding.sallonapp.data.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context

):LocationRepository {
    override  fun getLocation(): Context {
return context
    }
}