package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.Purple40
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: String,
    navController: NavController,
    likedBarberViewModel: LikedBarberViewModel,
    getBarberDataViewModel: GetBarberDataViewModel= hiltViewModel()
) {
    BackHandler {
        navController.popBackStack()
    }
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var active by remember {
        mutableStateOf(false)
    }
    val bookingModel = BookingModel()
    val timeLimit = remember {
        mutableStateOf(true)
    }

    val barberList by getBarberDataViewModel.barberListByState.collectAsState()
    val filteredList = barberList.filter {
        it.shopName!!.contains(text, ignoreCase = true) ||
                it.name!!.contains(text, ignoreCase = true)
    }
    LaunchedEffect(Unit) {
        getBarberDataViewModel.getBarberListByState(state)
        kotlinx.coroutines.delay(7000)
        timeLimit.value=false
    }
    if(barberList.isEmpty()&&timeLimit.value){
        LoadingAnimation()
    }
    if(!timeLimit.value){
        Card(
            modifier = Modifier.wrapContentSize().padding(25.dp),
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
    }
    Scaffold(
        containerColor = purple_200,
        topBar = {
            BackButtonTopAppBar(
                onBackClick = { navController.popBackStack() },
                title = "Search Barber"
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = { active = false },
                        expanded = active,
                        onExpandedChange = { active = it },
                        placeholder = { Text(text = "Search", color = Color.Black) },
                        leadingIcon =
                        {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Purple40

                            )
                        },
                        trailingIcon = {
                            if (active) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        if (text.isNotEmpty()) {
                                            text = ""
                                        } else {
                                            active = false
                                        }
                                    },
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Icon",
                                    tint = Color.Gray
                                )
                            }
                        },
                        colors = SearchBarDefaults.inputFieldColors(
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                        ),
                        interactionSource = null,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    )
                },
                expanded = active, onExpandedChange = { active = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .heightIn(min = 50.dp, max = 180.dp)
                    .padding(start = 24.dp, end = 24.dp),
                shape = RoundedCornerShape(12.dp), // Adjust the corner radius
                colors = SearchBarDefaults.colors(
                    containerColor = purple_200,
                    dividerColor = Color.Black
                ),
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                windowInsets = SearchBarDefaults.windowInsets,
            ) {
                filteredList.forEach { barber ->
                    Text(
                        text = "Shop Name: ${barber.shopName}",
                        Modifier
                            .clickable { text = barber.shopName.toString() }
                            .padding(start = 4.dp),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Barber Name: ${barber.name}",
                        Modifier
                            .clickable { text = barber.name.toString() }
                            .padding(start = 4.dp),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column (modifier = Modifier.verticalScroll(rememberScrollState())){


            filteredList.forEach { barber ->
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
                    imageUri = barber.imageUri!!,
                    address = barber.shopStreetAddress.toString() + barber.city.toString() + barber.state.toString(),
                    distance = barber.distance.toString().toDouble(),
                    numberOfReviews = barber.noOfReviews.toString().toInt(),
                    rating = barber.rating.toString().toDouble(),
                    onHeartClick = {
                        isLiked = if (isLiked) {
                            likedBarberViewModel.unlikeBarber(barber.uid)
                            false
                        } else {
                            likedBarberViewModel.likeBarber(barber.uid)
                            true
                        }
                    },
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
                    isFavorite = isLiked,
                    open = barber.open!!,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
        }}
    }
}