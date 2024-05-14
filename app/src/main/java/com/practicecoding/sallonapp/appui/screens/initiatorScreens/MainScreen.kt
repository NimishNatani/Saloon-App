package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.OfferCard
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LikedBarberViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LocationViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LikedBarber
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MainScreen(
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    locationViewModel.startLocationUpdates()
    val location by locationViewModel.getLocationLiveData().observeAsState()
    var locationDetails by remember {
        mutableStateOf(LocationModel(null, null, null, null, null))
    }
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address>? = location?.latitude?.let {
        geocoder.getFromLocation(
            it.toDouble(), location!!.longitude!!.toDouble(), 1
        )
    }
    var isDialog by remember {
        mutableStateOf(true)
    }
    if (isDialog) {
//        CircularProgress()
        CircularProgressWithAppLogo()
    }

    if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        locationDetails = LocationModel(
            location!!.latitude,
            location!!.longitude,
            address.locality,
            address.adminArea,
            address.countryName
        )
//        Toast.makeText(context,locationDetails.latitude,Toast.LENGTH_SHORT).show()
    }
    val scrollStateRowOffer = rememberScrollState()
    val scrollStateRowCategories = rememberScrollState()
    val scrollStateNearbySalon = rememberScrollState()
    val scope = rememberCoroutineScope()
    var barberPopularModel by initializeMultipleBarber()
    var barberNearbyModel by initializeMultipleBarber()

    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.Main) {
            isDialog = true
            delay(500)
            if (locationDetails.city != null) {
                barberNearbyModel =
                    viewModelBarber.getBarberNearby(locationDetails.city.toString(),6)
            }
            barberPopularModel = viewModelBarber.getBarberPopular(6)
            isDialog = false
        }.join()
    }
//    Toast.makeText(context,barberPopularModel[0].imageUri,Toast.LENGTH_SHORT).show()
    if (!isDialog) {
        Column(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                Categories(image = R.drawable.shave, categories = "Shave")
                Categories(image = R.drawable.makeup, categories = "Make Up")
                Categories(image = R.drawable.haircolor, categories = "Hair Color")
                Categories(image = R.drawable.hairspa, categories = "Hair Spa")
                Categories(image = R.drawable.nails, categories = "Nail Cut")
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                        value = locationDetails.city.toString()
                    )
                    navController.navigate(Screens.ViewAllScreen.route)
                }) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            Row(modifier = Modifier.horizontalScroll(scrollStateNearbySalon)) {
                for (barber in barberNearbyModel) {
                    var isBarberLiked by remember {
                        mutableStateOf(false)
                    }
                    BigSaloonPreviewCard(
                        shopName = barber.shopName.toString(),
                        imageUrl = barber.imageUri.toString(),
                        address = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                        distance = getLocation(lat1 =locationDetails.latitude!!.toDouble() , long1 =locationDetails.longitude!!.toDouble() , lat2 =barber.lat , long2 =barber.long ),
                        noOfReviews = barber.noOfReviews!!.toInt(),
                        rating = barber.rating,
                        onHeartClick = {
                               scope.launch(Dispatchers.IO) {
                                   val likedBarberList = likedBarberViewModel.getAllLikedBarbers.collect(){
                                       val likedBarber = it.find { likedBarber -> likedBarber.barberUid == barber.uid }
                                        if (likedBarber == null) {
                                             likedBarberViewModel.addOrUpdateLikedBarber(LikedBarber(barberUid = barber.uid))
                                        } else if(likedBarber.barberUid == barber.uid){
                                            likedBarberViewModel.deleteLikedBarber(likedBarber)
                                        }
                                   }
                               }
                        },
                        onBookNowClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "uid",
                                value = barber.uid
                            )
                            navController.navigate(route=Screens.BarberScreen.route, builder = {
                                // Specify the popUpTo and popUpToSavedState parameters
                                popUpTo(route = Screens.MainScreen.route) {
                                    inclusive = false // Exclude the HomeScreen from the popUpTo
                                }
                            })
                        },
                        isFavorite = isBarberLiked,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }


            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                        value = locationDetails.city.toString()
                    )
                    navController.navigate(Screens.ViewAllScreen.route)
                }) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            for (barber in barberPopularModel) {
                var isBarberLiked by remember {
                    mutableStateOf(false)
                }
                SmallSaloonPreviewCard(
                    shopName = barber.shopName.toString(),
                    imageUri = barber.imageUri.toString(),
                    address = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                    distance = getLocation(lat1 =locationDetails.latitude!!.toDouble() , long1 =locationDetails.longitude!!.toDouble() , lat2 =barber.lat , long2 =barber.long ),
                    numberOfReviews = barber.noOfReviews!!.toInt(),
                    rating = barber.rating,
                    onBookClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "uid",
                            value = barber.uid

                        )
                        navController.navigate(Screens.BarberScreen.route)
                    },
                    onHeartClick = {
                        scope.launch(Dispatchers.IO) {
                            val likedBarberList = likedBarberViewModel.getAllLikedBarbers.collect(){
                                val likedBarber = it.find { likedBarber -> likedBarber.barberUid == barber.uid }
                                if (likedBarber == null) {
                                    likedBarberViewModel.addOrUpdateLikedBarber(LikedBarber(barberUid = barber.uid))
                                } else if(likedBarber.barberUid == barber.uid){
                                    likedBarberViewModel.deleteLikedBarber(likedBarber)
                                }
                            }
                        }

                    },
                    isFavorite = isBarberLiked,
                    )
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
                    lat=0.0,
                    long = 0.0,
                    open = true
                )
            )
        )
    }
}
@Composable
fun getLocation(lat1:Double,long1:Double,lat2:Double,long2:Double):Double{
    val distance by remember {
        mutableStateOf(FloatArray(1))
    }
    Location.distanceBetween(lat1,long1,lat2,long2,distance)
    var solution = distance[0].toDouble()/1000
      solution = Math. round(solution * 10.0) / 10.0
    return solution
}


//@Preview(showBackground = true)
//@Composable
//fun AdvancedSignUpScreenPreview() {
//    val context = LocalContext.current
//    MainScreen()
//}