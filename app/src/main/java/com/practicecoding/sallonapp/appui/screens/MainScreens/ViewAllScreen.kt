package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.ShimmerEffect
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.ViewAllScreenViewModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ViewAllScreen(
    type: String,
    location: String,
    latitude: Double,
    longitude: Double,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    viewAllScreenViewModel: ViewAllScreenViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LaunchedEffect(key1 = true) {
        viewAllScreenViewModel.initializeBarber(
            viewModelBarber,
            location,
            type,
            latitude,
            longitude
        )
    }
    if (viewAllScreenViewModel.isDialog.value) {
        ShimmerEffect(screenWidth-50.dp,screenWidth-85.dp)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (barber in viewAllScreenViewModel.barbers.value) {
                var isLiked by remember {
                    mutableStateOf(
                        false
                    )
                }
                LaunchedEffect(isLiked) {
                    scope.launch(Dispatchers.IO) {
                        isLiked = likedBarberViewModel.isBarberLiked(barber.uid)
                    }
                }
                BigSaloonPreviewCard(
                    shopName = barber.shopName!!,
                    address = barber.shopStreetAddress!!,
                    rating = barber.rating,
                    noOfReviews = barber.noOfReviews!!.toInt(),
                    imageUrl = barber.imageUri!!,
                    onBookNowClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "barber",
                            value = barber
                        )
                        navController.navigate(
                            route = Screens.BarberScreen.route,
                        )
                    },
                    onHeartClick = {
                        isLiked = if (isLiked) {
                            likedBarberViewModel.unlikeBarber(barber.uid)
                            false
                        } else {
                            likedBarberViewModel.likeBarber(barber.uid)
                            true
                        }
                    },
                    modifier = Modifier,
                    distance = barber.distance!!,
                    open = barber.open!!,
                    width = screenWidth - 50.dp,
                    height = screenWidth - 85.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun sortBarber():Boolean{
    var isBottomBar by remember {
        mutableStateOf(false)
    }
    Row (Modifier.padding(horizontal = 15.dp,)){
        Text(text = "Sort", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier = Modifier
            .padding(start = 10.dp, top = 14.dp)
            .weight(1f))
        IconButton(onClick = { isBottomBar=true }) {
            Icon(painter = painterResource(id = R.drawable.sort), contentDescription = "",Modifier.size(24.dp))
        }
    }
    return isBottomBar
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(){
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var selectedSortOption by remember { mutableStateOf("Distance") }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            BottomSheetContent(selectedSortOption)
        },
        sheetPeekHeight = 0.dp
    ) {
        // Your main content goes here
    }

}

@Composable
fun BottomSheetContent(selected:String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Sort By", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
        Divider()
        sortOption("Distance", )
        sortOption("Rating", )
        sortOption("Number of Reviews", )
    }
}

@Composable
fun sortOption(option: String):String {
    var select by remember {
        mutableStateOf("NearBy")
    }
    Text(
        text = option,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { select = option }
    )
    return select
}

