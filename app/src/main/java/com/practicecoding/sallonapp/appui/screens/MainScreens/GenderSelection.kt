package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.AlertDialogBox
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.purple_400
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun GenderSelectOnBook(
    navController: NavController,
    bookingModel: BookingModel,
    onBackClick: () -> Unit,
    getBarberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
) {
    BackHandler {
        navController.popBackStack()
    }
    var genderCounter by getBarberDataViewModel.genderCounter
    val context = LocalContext.current
    if (getBarberDataViewModel.showDialog.value) {
        AlertDialogBox(
            getBarberDataViewModel.dialogMessage.value,
            onDismiss = { getBarberDataViewModel.showDialog.value = false },
            onClick = { getBarberDataViewModel.showDialog.value = false }
        )
    }
    if (getBarberDataViewModel.genderCounter.value.sum() > 4) {
        getBarberDataViewModel.showDialog.value = true
        getBarberDataViewModel.dialogMessage.value =
            listOf("Limit", "You can use this services for 4 people at a time")

    }
    Surface(modifier = Modifier.fillMaxSize(), color = purple_200) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(purple_200)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
            ) {
                Box {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                    Text(
                        text = "Gender Selection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    )
                }
//                BannerAds(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 25.dp),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    elevation = 0.dp
                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp),
//                    verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (bookingModel.barber.saloonType == "Male Salon" || bookingModel.barber.saloonType == "Unisex Salon") {
                            GenderCounter(
                                genderName = "Male",
                                genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                                count =
                                genderCounter[0],
                                onIncrement = {
                                    if (getBarberDataViewModel.genderCounter.value.sum() >= 4) {
                                        getBarberDataViewModel.showDialog.value = true
                                        getBarberDataViewModel.dialogMessage.value = listOf(
                                            "Limit",
                                            "You can use this services for 4 people at a time"
                                        )
                                    } else {
                                        genderCounter =
                                            genderCounter.toMutableList()
                                                .apply { this[0]++ }
                                    }
                                },
                                onDecrement = {
                                    genderCounter =
                                        genderCounter.toMutableList()
                                            .apply { if (this[0] > 0) this[0]-- }
                                }
                            )
                        }
                        if (bookingModel.barber.saloonType == "Female Salon" || bookingModel.barber.saloonType == "Unisex Salon") {

                            GenderCounter(
                                genderName = "Female",
                                genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                                count = getBarberDataViewModel.genderCounter.value[1],
                                onIncrement = {
                                    if (getBarberDataViewModel.genderCounter.value.sum() >= 4) {
                                        getBarberDataViewModel.showDialog.value = true
                                        getBarberDataViewModel.dialogMessage.value = listOf(
                                            "Limit",
                                            "You can use this services for 4 people at a time"
                                        )
                                    } else {
                                        genderCounter =
                                            genderCounter.toMutableList()
                                                .apply { this[1]++ }
                                    }
                                },
                                onDecrement = {
                                    genderCounter =
                                        genderCounter.toMutableList()
                                            .apply { if (this[1] > 0) this[1]-- }
                                }
                            )
                        }
                        GenderCounter(
                            genderName = "Other",
                            genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                            count = getBarberDataViewModel.genderCounter.value[2],
                            onIncrement = {
                                if (getBarberDataViewModel.genderCounter.value.sum() >= 4) {
                                    getBarberDataViewModel.showDialog.value = true
                                    getBarberDataViewModel.dialogMessage.value = listOf(
                                        "Limit",
                                        "You can use this services for 4 people at a time"
                                    )
                                } else {
                                    genderCounter =
                                        genderCounter.toMutableList()
                                            .apply { this[2]++ }
                                }
                            },
                            onDecrement = {
                                genderCounter =
                                    genderCounter.toMutableList()
                                        .apply { if (this[2] > 0) this[2]-- }
                            }
                        )

                    }
                }
            }
            GeneralButton(
                text = "Next",
                width = 160,
                height = 80,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                if (getBarberDataViewModel.genderCounter.value.sum() > 0) {
                    if (getBarberDataViewModel.genderCounter.value.sum() <= 4) {
                        getBarberDataViewModel.genderCounter.value = genderCounter
                        bookingModel.genderCounter = genderCounter
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "bookingModel",
                            value = bookingModel
                        )
                        navController.navigate(Screens.serviceSelector.route)
                    } else {
                        getBarberDataViewModel.showDialog.value = true
                        getBarberDataViewModel.dialogMessage.value =
                            listOf("Limit", "You can use this services for 4 people at a time")
                    }
                } else {
                    getBarberDataViewModel.showDialog.value = true
                    getBarberDataViewModel.dialogMessage.value =
                        listOf("No Selection", "Please select your gender")
                }
            }
        }
    }

}

@Composable
fun GenderCounter(
    genderName: String,
    genderImage: Int,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, sallonColor, RoundedCornerShape(10.dp)),
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = genderImage),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = genderName,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDecrement) {
                Icon(
                    painterResource(id = R.drawable.minus),
                    contentDescription = "Decrement",
                    modifier = Modifier.padding(bottom = 17.dp),
                    tint = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .background(purple_400, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = sallonColor,
                )
            }
            IconButton(onClick = onIncrement) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increment",
                    tint = Color.Black
                )
            }
        }
    }
}