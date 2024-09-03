package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.ShimmerBigCards
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BarberServiceVise(
    service: String,
    barberListViewModel: GetBarberDataViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController,
) {
    BackHandler {
        navController.popBackStack()
    }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            service.let {
                barberListViewModel.getBarberListByService(it)
            }
            isLoading = false
        }
    }


    val barberList = barberListViewModel.barberList
    Scaffold(
        containerColor = purple_200,
        topBar = {
            BackButtonTopAppBar(
                onBackClick = { navController.popBackStack() },
                title = service
            )
        }
    ) {
        AnimatedVisibility(
            isLoading, exit = fadeOut(animationSpec = tween(800, easing = EaseOut))
        ) {
            ShimmerBigCards(3,it)
        }
        if(!isLoading) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            items(barberList.value.size) {
                Spacer(modifier = Modifier.height(15.dp))
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
                    rating = barberList.value[it].rating,
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
            }
        }
    }
}
}