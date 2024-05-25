package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.CircularCheckbox
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarberBig
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.ViewAllScreenViewModel
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.purple_400
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ViewAllScreen(
    type: String,
    location: String,
    latitude: Double,
    longitude: Double,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(),
    viewAllScreenViewModel: ViewAllScreenViewModel = hiltViewModel(),
    likedBarberViewModel: LikedBarberViewModel,
    navController: NavController,
    sortType: String

) {
    BackHandler {
        navController.popBackStack()
    }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LaunchedEffect(key1 = true) {
        viewAllScreenViewModel.initializeBarber(
            viewModelBarber,
            location,
            type,
            latitude,
            longitude,

            )
    }
    if (viewAllScreenViewModel.isDialog.value) {
        for (i in 1 until 3) {
            ShimmerEffectBarberBig(screenWidth - 50.dp, screenWidth - 85.dp)
        }
    } else {
        when (sortType) {
            "Distance" -> {
                viewAllScreenViewModel.barbers.value =
                    viewAllScreenViewModel.barbers.value.sortedBy { it.distance }
            }

            "Rating" -> {

                viewAllScreenViewModel.barbers.value =
                    viewAllScreenViewModel.barbers.value.sortedBy { it.rating }.asReversed()

            }

            else -> {
                viewAllScreenViewModel.barbers.value =
                    viewAllScreenViewModel.barbers.value.sortedBy { it.noOfReviews!!.toInt() }
                        .asReversed()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (barber in viewAllScreenViewModel.barbers.value) {
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
                    shopName = barber.shopName!!,
                    address = barber.shopStreetAddress!!,
                    rating = barber.rating,
                    noOfReviews = barber.noOfReviews!!.toInt(),
                    imageUrl = barber.imageUri!!,
                    onBookNowClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "barber",
                            value = barber
                        )
                        navController.navigate(
                            route = Screens.BarberScreen.route,
                        )
                    },
                    onHeartClick = {
                        isLiked = if (isLiked) {
                            likedBarberViewModel.unlikeBarber(barber.uid)
                            false
                        } else {
                            likedBarberViewModel.likeBarber(barber.uid)
                            true
                        }
                    },
                    modifier = Modifier,
                    distance = barber.distance!!,
                    open = barber.open!!,
                    width = screenWidth - 50.dp,
                    height = screenWidth - 85.dp,
                    isFavorite = isLiked
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SortBarber(onSortClick: () -> Unit) {
    Row(Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Sort",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 15.dp, top = 14.dp)
                .weight(1f)
        )
        IconButton(onClick = onSortClick) {
            Icon(
                painter = painterResource(id = R.drawable.sort),
                contentDescription = "",
                Modifier.size(24.dp), tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    initialSortType: String,
    onSortTypeChange: (String) -> Unit
) {
    var selectedSortOption by remember { mutableStateOf(initialSortType) }

    BottomSheetScaffold(
        sheetContainerColor = purple_400,
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            BottomSheetContent(selectedSortOption = selectedSortOption) { newSortType ->
                selectedSortOption = newSortType
                onSortTypeChange(newSortType)
                onDismiss()
            }
        },
        sheetPeekHeight = 250.dp
    ) {}
    LaunchedEffect(selectedSortOption) {
        onSortTypeChange(selectedSortOption)
    }
}

@Composable
fun BottomSheetContent(selectedSortOption: String, onSortTypeChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Sort By",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.Black
        )
        HorizontalDivider()
        SortOption("Distance", selectedSortOption, onSortTypeChange)
        SortOption("Rating", selectedSortOption, onSortTypeChange)
        SortOption("Number of Reviews", selectedSortOption, onSortTypeChange)
    }
}

@Composable
fun SortOption(option: String, selectedSortOption: String, onSortTypeChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = option,
            fontSize = 18.sp,
            color = if (option == selectedSortOption) sallonColor else Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        CircularCheckbox(
            isServiceSelected = option == selectedSortOption,
            onClick = { onSortTypeChange(option) },
            modifier = Modifier
                .size(36.dp)
                .padding(top = 8.dp, bottom = 8.dp, end = 15.dp),
            color = if (option == selectedSortOption) sallonColor else Color.Transparent
        )
    }
}

