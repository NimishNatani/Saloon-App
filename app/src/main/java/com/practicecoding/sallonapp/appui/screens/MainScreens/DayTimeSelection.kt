package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.util.Log
import androidx.compose.runtime.Composable
import com.practicecoding.sallonapp.data.model.Service

@Composable
fun DayTimeSelection(service:List<Service>?){
    Log.d("service",service.toString())
}