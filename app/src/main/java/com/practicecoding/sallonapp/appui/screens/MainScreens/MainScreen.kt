package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.BottomAppNavigationBar
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.appui.components.OfferCard
import com.practicecoding.sallonapp.appui.components.ProfileWithNotification
import com.practicecoding.sallonapp.appui.components.SearchBar
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarber
import com.practicecoding.sallonapp.appui.components.ShimmerEffectMainScreen
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.UpcomingFeaturesCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LocationViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MainEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.data.model.locationObject
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MainScreen1(
    navHostController: NavController,
    context: Context,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    messageViewModel: MessageViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    val newChat by messageViewModel.newChat.collectAsState()
    locationViewModel.startLocationUpdates()
    val location by locationViewModel.getLocationLiveData().observeAsState()

    val geocoder = Geocoder(context, Locale.getDefault())
    var locationDetails = locationObject.locationDetails
    val context = LocalContext.current
    LaunchedEffect(location) {
        val addresses: List<Address>? = location?.latitude?.let {
            geocoder.getFromLocation(it.toDouble(), location!!.longitude!!.toDouble(), 1)
        }

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            locationObject.locationDetails = LocationModel(
                location!!.latitude,
                location!!.longitude,
                address.locality,
                address.adminArea,
                address.countryName
            )
            locationDetails = locationObject.locationDetails
        }
        if (locationDetails.city != null) {
//            Toast.makeText(context,locationDetails.city,Toast.LENGTH_LONG).show()
            viewModelBarber.onEvent(
                MainEvent.getBarberNearby(
                    locationDetails.city!!,
                    6
                )
            )
            viewModelBarber.onEvent(
                MainEvent.getBarberPopular(
                    locationDetails.city!!,
                    6
                )
            )
        }
    }
//    var selectedScreen by remember { mutableStateOf() }
    Scaffold(
        bottomBar = {
            BottomAppNavigationBar(
                selectedItem = viewModelBarber.navigationItem.value,
                onItemSelected = { viewModelBarber.navigationItem.value = it },
                messageCount =newChat
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            when (viewModelBarber.navigationItem.value) {
                NavigationItem.Home -> {
                    viewModelBarber.navigationItem.value = NavigationItem.Home
                    TopScreen(
                        navHostController,
                        context,
                        viewModelBarber
                    )
                }

                NavigationItem.Book -> {
                if (orderViewModel.isLoading.value) {
                    ShimmerEffectBarber()
                } else {
                    viewModelBarber.navigationItem.value = NavigationItem.Book
                    UserOrderPage(
                        navController = navHostController,
                        orderViewModel= orderViewModel,
                        viewModelBarber = viewModelBarber
                    )
                }
                }
                NavigationItem.Message -> {
                    viewModelBarber.navigationItem.value = NavigationItem.Message
                    MessageScreen(navHostController, messageViewModel,viewModelBarber)
                }
                NavigationItem.Profile -> {
                    viewModelBarber.navigationItem.value = NavigationItem.Profile

                    ProfileScreen(viewModelBarber, navHostController)
                }
                NavigationItem.More -> {
                    viewModelBarber.navigationItem.value = NavigationItem.More

                    MoreScreen()
                }
            }
        }
    }
}

@Composable
fun MoreScreen() {
    Text(text = "More Screen")
}

@Composable
fun TopScreen(
    navController: NavController,
    context: Context,
    viewModelBarber: GetBarberDataViewModel,

) {
    DoubleCard(
        midCarBody = { SearchBar() },
        mainScreen = {
            MainScreen(
                navController = navController,
                likedBarberViewModel = LikedBarberViewModel(context),
                viewModelBarber = viewModelBarber,

            )
        },
        topAppBar = {
            ProfileWithNotification(
                onProfileClick = { /*TODO*/ },
                onNotificationClick = { /*TODO*/ },
            )
        },
    )
}

@Composable
fun MainScreen(
    viewModelBarber: GetBarberDataViewModel,
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController,

) {
    val scrollStateRowOffer = rememberScrollState()
    val scroll = rememberScrollState()
    val scrollStateRowCategories = rememberScrollState()
    val scrollStateNearbySalon = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val barberNearby by
        viewModelBarber.barberNearby.collectAsState()
    val barberPopular by
        viewModelBarber.barberPopular.collectAsState()
    val bookingModel = BookingModel()
//    Toast.makeText(context,viewModelBarber.barberPopular.collectAsState().value.toString(),Toast.LENGTH_LONG).show()

    AnimatedVisibility(
        (barberNearby.isEmpty() || barberPopular.isEmpty()),
        exit = fadeOut(animationSpec = tween(800, easing = EaseOut))
    ) {
        ShimmerEffectMainScreen()
    }
    AnimatedVisibility(
        (barberNearby.isNotEmpty() && barberPopular.isNotEmpty()), enter = fadeIn(
            // Slide in from 40 dp from the top.
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp,
//                        bottom = 64.dp
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
                    TextButton(onClick = {
                        navController.navigate(Screens.AllCategory.route)
                    }) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                Row(modifier = Modifier.horizontalScroll(scrollStateRowCategories)) {
                    Categories(image = R.drawable.haircut, categories = "Hair Cut") {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = "Hair Cut"
                        )
                        navController.navigate(Screens.CatBarberList.route)
                    }
                    Categories(image = R.drawable.shaving, categories = "Bleach") {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = "Bleach"
                        )
                        navController.navigate(Screens.CatBarberList.route)
                    }
                    Categories(image = R.drawable.makeup, categories = "Clean Up") {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = "Clean Up"
                        )
                        navController.navigate(Screens.CatBarberList.route)
                    }
                    Categories(image = R.drawable.haircolor, categories = "Hair Color") {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = "Hair Color"
                        )
                        navController.navigate(Screens.CatBarberList.route)
                    }
                    Categories(image = R.drawable.nails, categories = "Nail Cut") {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "service",
                            value = "Nail Cut"
                        )
                        navController.navigate(Screens.CatBarberList.route)
                    }
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
                            value = locationObject.locationDetails.city.toString()
                        )
                        navController.navigate(Screens.ViewAllScreen.route)
                    }) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                Row(modifier = Modifier.horizontalScroll(scrollStateNearbySalon)) {
                    for (barber in barberNearby) {
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
                                bookingModel.barber = barber
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "bookingModel",
                                    value = bookingModel
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
                            value = locationObject.locationDetails.city.toString()
                        )
                        navController.navigate(Screens.ViewAllScreen.route)
                    }) {
                        Text(text = "View All", color = Color.Gray)
                    }

                }
                for (barber in barberPopular) {
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
                            bookingModel.barber = barber
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "bookingModel",
                                value = bookingModel
                            )
                            navController.navigate(
                                route = Screens.BarberScreen.route,
                            )
                        },
                        open = barber.open!!,
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
                UpcomingFeaturesCard()
            }
        }
    }
}