package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BookingScreenShopPreviewCard
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.GenderSelectCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.HorizontalPagerWithTabs
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.data.model.ServiceModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BarberScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    barber:BarberModel,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel()
) {
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
    var isDialog by remember {
        mutableStateOf(true)
    }
    if (isDialog) {
        CircularProgressWithAppLogo()
    }
//    var barber by initializeSingleBarber()
    var services:List<ServiceCat> by initializeServices()

    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.Main) {
            isDialog = true
            delay(500)
//            barber = viewModelBarber.getBarber(uid)
            services = viewModelBarber.getServices(barber.uid)
            isDialog = false
        }.join()
    }
    if (!isDialog) {
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
//                            .aspectRatio(1.0f),
                            contentScale = ContentScale.FillBounds,
                            )
                    }

                    TransparentTopAppBar(
                        onBackClick = { /*TODO*/ },
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
//                        .align(Alignment.BottomCenter)
                        .background(purple_200)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 8.dp)
//                            .verticalScroll(rememberScrollState())
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
                                    shopAddress = barber.shopStreetAddress.toString() + barber?.city.toString() + barber?.state.toString(),
                                    ratings = barber.rating.toDouble(),
                                    numberOfReviews = barber.noOfReviews!!.toInt(),
                                ) {

                                }
//                                Spacer(modifier = Modifier.height(8.dp))
                                // Second card
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    HorizontalPagerWithTabs(
                                        barber,
                                        services,
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

//                    .noRippleClickable { onShareClick() }
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
                            value = services
                        )
                        navController.navigate(Screens.GenderSelection.route)
                    }
                }
            }
        }
    }
}

@Composable
fun GenderSelectOnBook(navController:NavController,service: List<ServiceCat>
) {
    var isSelect by remember { mutableStateOf(mutableListOf<Boolean>(false,false,false)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = purple_200),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GenderSelectCard(gender = "Male", isSelect = isSelect,onThis = isSelect[0])
                Spacer(modifier = Modifier.width(8.dp))
                GenderSelectCard(gender = "Female", isSelect = isSelect,onThis = isSelect[1])
            }
            GenderSelectCard(gender = "Other", isSelect = isSelect,onThis = isSelect[2])
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            backgroundColor = Color.White,
            contentColor = Color.Black,
            elevation = 8.dp
        ) {
            GeneralButton(
                text = "Next",
                width = 160,
                height = 80,
                modifier = Modifier.fillMaxWidth(),
                roundnessPercent = 45,
            ) {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "service",
                    value = service

                )
                navController.navigate(Screens.serviceSelector.route)
            }
        }
    }
}

@Composable
fun initializeSingleBarber(): MutableState<BarberModel?> {
    return remember {
        mutableStateOf(
            BarberModel(
                shopName = "Salon 1",
                state = "Karnataka",
                city = "Bangalore",
                shopStreetAddress = "Shop No 1, 1st Floor, 1st Cross, 1st Main, 1st Block",
                imageUri = "",
                aboutUs = "We are the best salon in Bangalore",
                noOfReviews = "10",
                rating = 4.2,
                uid = "",
                lat=0.0,
                long=0.0
            )
        )
    }
}
@Composable
fun initializeServices(): MutableState<List<ServiceCat>> {
    return remember {
        mutableStateOf(
           listOf(
            ServiceCat(
              type = null,
                services = listOf()
            )
        ))
    }
}
