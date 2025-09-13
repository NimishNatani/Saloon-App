package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BookingScreenShopPreviewCard
import com.practicecoding.sallonapp.appui.components.HorizontalPagerWithTabs
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarber
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MainEvent
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BarberScreen(
    navController: NavController,

    bookingModel: BookingModel,
    likedBarberViewModel: LikedBarberViewModel,
    getBarberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
) {
    BackHandler {
        navController.popBackStack()
    }
    val previewImages = listOf(
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636",
    )
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
    val services by getBarberDataViewModel.services.collectAsState()
    val reviewList by getBarberDataViewModel.barberReviewList.collectAsState()
    val context = LocalContext.current
    var isLiked by remember {
        mutableStateOf(
            false
        )
    }
    LaunchedEffect(isLiked) {
CoroutineScope(Dispatchers.IO).launch {
    isLiked = likedBarberViewModel.isBarberLiked(bookingModel.barber.uid)
}
    }

    LaunchedEffect(key1 = true) {
        getBarberDataViewModel.onEvent(MainEvent.getServices(bookingModel.barber.uid))
        getBarberDataViewModel.getReviewList(bookingModel.barber.uid)

    }
    AnimatedVisibility(
        services.isEmpty(), exit = fadeOut(animationSpec = tween(800, easing = EaseOut))
    ) {
        ShimmerEffectBarber()
    }
    AnimatedVisibility(
        services.isNotEmpty(), enter = fadeIn(
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(), color = purple_200
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(purple_200),
                ) {
                    // Background image
                    Surface(
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .padding(top = 6.dp, start = 6.dp, end = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = bookingModel.barber.imageUri.toString()
                            ),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                    TransparentTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        onLikeClick = {    isLiked = if (isLiked) {
                            likedBarberViewModel.unlikeBarber(bookingModel.barber.uid)
                            false
                        } else {
                            likedBarberViewModel.likeBarber(bookingModel.barber.uid)
                            true
                        } },
                        isFavorite = isLiked,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(purple_200)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                            .align(Alignment.TopCenter)
                            .background(purple_200),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        // Main card
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(2.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BookingScreenShopPreviewCard(
                                    bookingModel = bookingModel,
                                    onOpenClick = { bookingModel.services = services
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            key = "bookingModel",
                                            value = bookingModel
                                        )
                                        navController.navigate(Screens.GenderSelection.route)},
                                    onDirectionClick = {
                                        val strUri = "https://maps.google.com/maps?q=loc:"+bookingModel.barber.lat+"," + bookingModel.barber.long+" (barber Location)"
                                        val intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri))
                                        intent.setPackage("com.google.android.apps.maps")
                                        context.startActivity(intent)
                                    }
                                )
                                // Second card
                                Card(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    HorizontalPagerWithTabs(
                                        bookingModel,
                                        services,
                                        previewImages, reviewList
                                    )

                                }
                            }
                        }
                    }
                }
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
//                    ,
//                    elevation = 8.dp,
//                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//                ) {
//                    GeneralButton(
//                        text = "Book now",
//                        width = 160,
//                        height = 80,
//                        modifier = Modifier.fillMaxWidth(),
//                        roundnessPercent = 35,
//                    ) {
//                        bookingModel.services=services
//                        navController.currentBackStackEntry?.savedStateHandle?.set(
//                            key = "bookingModel",
//                            value = bookingModel
//                        )
//                        navController.navigate(Screens.GenderSelection.route)
//                    }
//                }
            }
        }
    }
}