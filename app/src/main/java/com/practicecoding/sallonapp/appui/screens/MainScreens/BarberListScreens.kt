package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.practicecoding.sallonapp.appui.components.ShimmerBigCards
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarberBig
import com.practicecoding.sallonapp.appui.components.ShimmerEffectMainScreen
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.BarberServiceViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BarberServiceVise(
    service: String,
    barberListViewModel: BarberServiceViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController,
){
    var isLoading by remember {mutableStateOf(true)}
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            service?.let {
                barberListViewModel.getBarberListByService(it)
            }
            isLoading = false
        }
    }
    if(isLoading){
        ShimmerBigCards(int = 3)
    }else{
        val barberList = barberListViewModel.barberList
        Scaffold(
            topBar = {
                TransparentTopAppBar(
                    onBackClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.navigate(route = Screens.MainScreen.route,){
                                popUpTo(Screens.CatBarberList.route){
                                    inclusive = false
                                }
                            }
                        } else {
                            // Handle the case where there's no previous back stack entry
                        }
                                  },
                    onLikeClick = { /*TODO*/ },
                    onShareClick = { /*TODO*/ },
                    isFavorite = false,
                    usingInProfile = true
                )
            }
        ){
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{
                    Text(
                        text = service,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black
                    )
                }
                items(barberList.value.size) {
                    var isLiked by remember {
                        mutableStateOf(
                            false
                        )
                    }
                    LaunchedEffect(isLiked) {
                        scope.launch(Dispatchers.IO) {
                            isLiked = likedBarberViewModel.isBarberLiked(barberList.value[it].uid)
                        }
                    }
                    BigSaloonPreviewCard(
                        shopName = barberList.value[it].shopName!!,
                        address = barberList.value[it].shopStreetAddress!!,
                        distance = barberList.value[it].distance!!,
                        height = screenWidth - 70.dp,
                        width = screenWidth - 60.dp,
                        imageUrl = barberList.value[it].imageUri!!,
                        isFavorite = isLiked,
                        noOfReviews = barberList.value[it].noOfReviews!!.toInt(),
                        rating = barberList.value[it].rating!!,
                        onHeartClick = {
                            isLiked = if (isLiked) {
                                likedBarberViewModel.unlikeBarber(barberList.value[it].uid)
                                false
                            } else {
                                likedBarberViewModel.likeBarber(barberList.value[it].uid)
                                true
                            }
                        },
                        onBookNowClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "barber",
                                value = barberList.value[it]
                            )
                            navController.navigate(
                                route = Screens.BarberScreen.route,
                            )
                        },
                        modifier = Modifier,
                        open = barberList.value[it].open!!,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}