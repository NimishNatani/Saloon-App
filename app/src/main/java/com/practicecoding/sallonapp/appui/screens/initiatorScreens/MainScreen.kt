package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import java.util.Locale
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.OfferCard
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LocationViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
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
        mutableStateOf(false)
    }
    if (isDialog) {
//        CircularProgress()
        LoadingAnimation()
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
//        Toast.makeText(context,locationDetails.city,Toast.LENGTH_SHORT).show()
    }
    val scrollStateRowOffer = rememberScrollState()
    val scrollStateRowCategories = rememberScrollState()
    val scrollStateNearbySalon = rememberScrollState()
    val scope = rememberCoroutineScope()
    var barberPopularModel by initializeBarberPopularModel()
    var barberNearbyModel by initializeBarberPopularModel()

    LaunchedEffect(key1 = true) {
        scope.launch(Dispatchers.Main) {
            isDialog = true
            delay(500)
            if (locationDetails.city != null) {
                barberNearbyModel =
                    viewModelBarber.getBarberNearby(locationDetails.city.toString())
            }
            barberPopularModel = viewModelBarber.getBarberPopular()
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
                TextButton(onClick = {}) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            Row(modifier = Modifier.horizontalScroll(scrollStateNearbySalon)) {
                for (barber in barberNearbyModel) {
                    BigSaloonPreviewCard(
                        shopName = barber.shopName.toString(),
                        imageUrl = barber.imageUri.toString(),
                        address = barber.shopStreetAddress.toString(),
                        distance = 5.5,
                        noOfReviews = 10,
                        rating = 4.5,
                        onHeartClick = { },
                        onBookNowClick = {},
                        isFavorite = true,
                        modifier = Modifier
                    )
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
                TextButton(onClick = {}) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            for (barber in barberPopularModel) {
                SmallSaloonPreviewCard(shopName = barber.shopName.toString(),
                    imageUri = barber.imageUri.toString(),
                    address = barber.shopStreetAddress.toString(),
                    distance = 5.5,
                    numberOfReviews = 10,
                    rating = 4.5,
                    onBookClick = { })
            }

        }
    }
}

@Composable
fun initializeBarberPopularModel(): MutableState<List<BarberModel>> {
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
                    noOfReviews = "10",
                    rating = 4.2
                )
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AdvancedSignUpScreenPreview() {
    val context = LocalContext.current
    MainScreen()
}