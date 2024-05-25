package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BookingScreenShopPreviewCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.HorizontalPagerWithTabs
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarber
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.BarberScreenViewModel
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.ui.theme.purple_200

@Composable
fun BarberScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    barber: BarberModel,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    barberScreenViewModel: BarberScreenViewModel = hiltViewModel()
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
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        barberScreenViewModel.initializeData(viewModelBarber, barber.uid)

    }
    if (barberScreenViewModel.isDialog.value) {
        ShimmerEffectBarber()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize(), color = purple_200
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                                model = barber.imageUri.toString()
                            ),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                    TransparentTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        onLikeClick = { /*TODO*/ },
                        onShareClick = { /*TODO*/ },
                        isFavorite = false,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    )
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(purple_200)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 8.dp)
                            .align(Alignment.TopCenter)
                            .background(purple_200),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        // Main card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeight.dp * 0.21f),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(2.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BookingScreenShopPreviewCard(
                                    shopName = barber.shopName.toString(),
                                    shopAddress = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                                    ratings = barber.rating.toDouble(),
                                    numberOfReviews = barber.noOfReviews!!.toInt(),
                                    isOpen = barber.open!!
                                ) {

                                }
                                // Second card
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    HorizontalPagerWithTabs(
                                        barber,
                                        barberScreenViewModel.services.value,
                                        previewImages
                                    )

                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                    ,
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    GeneralButton(
                        text = "Book now",
                        width = 160,
                        height = 80,
                        modifier = Modifier.fillMaxWidth(),
                        roundnessPercent = 35,
                    ) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = barberScreenViewModel.services.value
                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "barber",
                            value = barber
                        )
                        navController.navigate(Screens.GenderSelection.route)
                    }
                }
            }
        }
    }
}