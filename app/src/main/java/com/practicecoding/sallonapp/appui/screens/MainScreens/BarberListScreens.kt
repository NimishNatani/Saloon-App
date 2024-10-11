package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
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
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        isLoading = true
        service.let {
            barberListViewModel.getBarberListByService(it)
        }
        kotlinx.coroutines.delay(10000)  // 15 seconds delay
        barberListViewModel.shimmerVisible.value = false
        isLoading = false
    }


    val barberList by barberListViewModel.barberListByService.collectAsState()
    val bookingModel = BookingModel()

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
            (barberListViewModel.shimmerVisible.value && (barberList.isEmpty())),
            exit = fadeOut(animationSpec = tween(800, easing = EaseOut))
        ) {
            ShimmerBigCards(3, it)
        }
        AnimatedVisibility(
            (!barberListViewModel.shimmerVisible.value || (barberList.isNotEmpty())),
            enter = fadeIn(
                // Slide in from 40 dp from the top.
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioLowBouncy
                )
            )
        ) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (barberList.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        colors = CardDefaults.cardColors(containerColor = purple_200)
                    ) {
                        Text(
                            text = "Sorry, we are unable to fetch your city barber",
                            color = sallonColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                    }
                } else {

                    barberList.forEach {
                        Spacer(modifier = Modifier.height(15.dp))
                        var isLiked by remember {
                            mutableStateOf(
                                false
                            )
                        }
                        LaunchedEffect(isLiked) {
                            scope.launch(Dispatchers.IO) {
                                isLiked = likedBarberViewModel.isBarberLiked(it.uid)
                            }
                        }
                        BigSaloonPreviewCard(
                            shopName = it.shopName!!,
                            address = it.shopStreetAddress!!,
                            distance = it.distance!!,
                            height = screenWidth - 70.dp,
                            width = screenWidth - 60.dp,
                            imageUrl = it.imageUri!!,
                            isFavorite = isLiked,
                            noOfReviews = it.noOfReviews!!.toInt(),
                            rating = it.rating,
                            onHeartClick = {
                                isLiked = if (isLiked) {
                                    likedBarberViewModel.unlikeBarber(it.uid)
                                    false
                                } else {
                                    likedBarberViewModel.likeBarber(it.uid)
                                    true
                                }
                            },
                            onBookNowClick = {
                                bookingModel.barber = it
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "bookingModel",
                                    value = bookingModel
                                )
                                navController.navigate(
                                    route = Screens.BarberScreen.route,
                                )
                            },
                            modifier = Modifier,
                            open = it.open!!,
                        )
                    }
                }
            }
        }
    }
}