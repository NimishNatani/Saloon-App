package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FavBarberListScreen(
    getBarberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController // Ensure this is passed to the Composable
) {
    val scope = rememberCoroutineScope()
    val likedBarberList by likedBarberViewModel.likedBarberList.collectAsState(initial = emptyList())

    // Launch the coroutine in scope, but switch to Main dispatcher when accessing UI state
    LaunchedEffect(likedBarberList) {
        scope.launch {
            withContext(Dispatchers.Main) {
                getBarberDataViewModel.getBarberListById(likedBarberList.map { it.uid })
            }
        }
    }

    Log.d("FavBarberListScreen", "FavBarberListScreen: $likedBarberList")
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onBackClick = {
                    navController.navigate(route = Screens.MainScreen.route,){
                        popUpTo(Screens.BookingHistory.route){
                            inclusive = false
                        }
                    }},
                onLikeClick = { /*TODO*/ },
                onShareClick = { /*TODO*/ },
                isFavorite = false,
                usingInProfile = true
            )
        }
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it).background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Text(
                    text = "Liked Barbers",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(getBarberDataViewModel.likedBarberList.value) { barber ->
                var isLiked by remember { mutableStateOf(true) }

                BigSaloonPreviewCard(
                    shopName = barber.shopName!!,
                    address = barber.shopStreetAddress!!,
                    distance = barber.distance!!,
                    height = screenWidth - 70.dp,
                    width = screenWidth - 60.dp,
                    imageUrl = barber.imageUri!!,
                    isFavorite = isLiked,
                    noOfReviews = barber.noOfReviews!!.toInt(),
                    rating = barber.rating!!,
                    onHeartClick = {
                        isLiked = if (isLiked) {
                            likedBarberViewModel.unlikeBarber(barber.uid)
                            false
                        } else {
                            likedBarberViewModel.likeBarber(barber.uid)
                            true
                        }
                    },
                    onBookNowClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "barber",
                            value = barber
                        )
                        navController.navigate(
                            route = Screens.BarberScreen.route,
                        )
                    },
                    modifier = Modifier,
                    open = barber.open!!,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}
