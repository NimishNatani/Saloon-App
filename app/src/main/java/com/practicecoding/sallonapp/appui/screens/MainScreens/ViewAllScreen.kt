package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.initializeMultipleBarber
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ViewAllScreen(
    type: String,
    location: String,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel()
) {
    var isDialog by remember {
        mutableStateOf(false)
    }
    if (isDialog) {
//        CircularProgress()
        LoadingAnimation()
    }
    val scope = rememberCoroutineScope()
    var barber by initializeMultipleBarber()
    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.Main) {
            isDialog = true
            delay(500)
            barber = if (type == "NearBy") {
                viewModelBarber.getBarberNearby(location, 15)
            } else {
                viewModelBarber.getBarberPopular(15)
            }

            isDialog = false
        }.join()
    }


}

