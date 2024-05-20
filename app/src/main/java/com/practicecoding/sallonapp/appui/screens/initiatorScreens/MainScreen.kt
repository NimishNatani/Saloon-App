package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.BottomAppNavigationBar
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.OfferCard
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LocationViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MainScreenViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.room.Dao
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MainScreen(
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel= hiltViewModel()
) {
    val context = LocalContext.current
    locationViewModel.startLocationUpdates()
    val location by locationViewModel.getLocationLiveData().observeAsState()

    val geocoder = Geocoder(context, Locale.getDefault())

    val scrollStateRowOffer = rememberScrollState()
    val scroll = rememberScrollState()
    val scrollStateRowCategories = rememberScrollState()
    val scrollStateNearbySalon = rememberScrollState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(location) {
        val addresses: List<Address>? = location?.latitude?.let {
            geocoder.getFromLocation(it.toDouble(), location!!.longitude!!.toDouble(), 1)
        }

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            mainScreenViewModel.locationDetails.value = LocationModel(
                location!!.latitude,
                location!!.longitude,
                address.locality,
                address.adminArea,
                address.countryName
            )
        }

        mainScreenViewModel.initializeData(viewModelBarber, mainScreenViewModel.locationDetails.value)

    }
    if (mainScreenViewModel.isDialog.value) {
        CircularProgressWithAppLogo()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 64.dp
                    )
                    .fillMaxSize()
                    .verticalScroll(scroll)
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollStateRowOffer)
                        .padding(end = 18.dp)
                ) {
                    OfferCard(
                        detailText = "hello",
                        percentOff = 30,
                        iconImageId = R.drawable.salon_app_logo,
                        onExploreClick = {},
                        cardColor = sallonColor
                    )
                    OfferCard(
                        detailText = "hello",
                        percentOff = 30,
                        iconImageId = R.drawable.salon_app_logo,
                        onExploreClick = {},
                        cardColor = sallonColor
                    )
                    OfferCard(
                        detailText = "hello",
                        percentOff = 30,
                        iconImageId = R.drawable.salon_app_logo,
                        onExploreClick = {},
                        cardColor = sallonColor
                    )
                    OfferCard(
                        detailText = "hello",
                        percentOff = 30,
                        iconImageId = R.drawable.salon_app_logo,
                        onExploreClick = {},
                        cardColor = sallonColor
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Categories",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 12.dp,
                            bottom = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        )

                    )
                    TextButton(onClick = {}) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                Row(modifier = Modifier.horizontalScroll(scrollStateRowCategories)) {
                    Categories(image = R.drawable.haircut, categories = "Hair Cut")
                    Categories(image = R.drawable.shaving, categories = "Shaving")
                    Categories(image = R.drawable.makeup, categories = "Make Up")
                    Categories(image = R.drawable.haircolor, categories = "Hair Color")
                    Categories(image = R.drawable.nails, categories = "Nail Cut")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nearby Salons",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 12.dp,
                            bottom = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        )

                    )
                    TextButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "type",
                            value = "NearBy"
                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "location",
                            value = mainScreenViewModel.locationDetails.value.city.toString()
                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "latitude",
                            value = mainScreenViewModel.locationDetails.value.latitude!!.toDouble()

                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "longitude",
                            value = mainScreenViewModel.locationDetails.value.longitude!!.toDouble()
                        )
                        navController.navigate(Screens.ViewAllScreen.route)
                    }) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                Row(modifier = Modifier.horizontalScroll(scrollStateNearbySalon)) {
                    for (barber in mainScreenViewModel.barberNearbyModel.value) {
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
                            shopName = barber.shopName.toString(),
                            imageUrl = barber.imageUri.toString(),
                            address = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                            distance = barber.distance!!,
                            noOfReviews = barber.noOfReviews!!.toInt(),
                            rating = barber.rating,
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
                            isFavorite = isLiked,
                            modifier = Modifier,
                            open = barber.open!!,
                            width = 280.dp,
                            height = 270.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }


                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Popular Saloon",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 12.dp,
                            bottom = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        )

                    )
                    TextButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "type",
                            value = "Popular"

                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "location",
                            value = mainScreenViewModel.locationDetails.value.city.toString()
                        )
                     navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "latitude",
                            value = mainScreenViewModel.locationDetails.value.latitude!!.toDouble()

                        )
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "longitude",
                            value = mainScreenViewModel.locationDetails.value.longitude!!.toDouble()
                        )
                        navController.navigate(Screens.ViewAllScreen.route)
                    }) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                for (barber in mainScreenViewModel.barberPopularModel.value) {
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
                    SmallSaloonPreviewCard(
                        shopName = barber.shopName.toString(),
                        imageUri = barber.imageUri.toString(),
                        address = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                        distance = barber.distance!!,
                        numberOfReviews = barber.noOfReviews!!.toInt(),
                        rating = barber.rating,
                        onBookClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "barber",
                                value = barber
                            )
                            navController.navigate(Screens.BarberScreen.route)
                        }, open = barber.open!!,
                        onHeartClick = {
                            isLiked = if (isLiked) {
                                likedBarberViewModel.unlikeBarber(barber.uid)
                                false
                            } else {
                                likedBarberViewModel.likeBarber(barber.uid)
                                true
                            }
                        },
                        isFavorite = isLiked,
                    )
                }

            }
        }
    }
}

@Composable
fun initializeMultipleBarber(): MutableState<List<BarberModel>> {
    return remember {
        mutableStateOf(
            mutableListOf(
                BarberModel(
                    shopName = "Salon 1",
                    state = "Karnataka",
                    city = "Bangalore",
                    shopStreetAddress = "Shop No 1, 1st Floor, 1st Cross, 1st Main, 1st Block",
                    imageUri = "",
                    aboutUs = "We are the best salon in Bangalore",
                    noOfReviews = "0",
                    rating = 4.2,
                    uid = "",
                    lat = 0.0,
                    long = 0.0,
                    open = true,
                    distance = 0.0
                )
            )
        )
    }
}