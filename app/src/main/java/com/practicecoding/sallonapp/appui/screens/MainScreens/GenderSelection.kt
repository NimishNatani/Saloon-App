package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.purple_400
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun GenderSelectOnBook(
    navController: NavController,
    service: List<ServiceCat>,
    barber: BarberModel,
    onBackClick: () -> Unit,
    viewModel: GetBarberDataViewModel= hiltViewModel(),
) {
    BackHandler {
        navController.popBackStack()
    }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize(), color = purple_200) {
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

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
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
                    if (barber.saloonType == "Male Salon" || barber.saloonType == "Hybrid Salon") {
                        GenderCounter(
                            genderName = "Male",
                            genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                            count =
                            viewModel.genderCounter.value[0],
                            onIncrement = {
                                viewModel.genderCounter.value =
                                    viewModel.genderCounter.value.toMutableList()
                                        .apply { this[0]++ }
                            },
                            onDecrement = {
                                viewModel.genderCounter.value =
                                    viewModel.genderCounter.value.toMutableList()
                                        .apply { if (this[0] > 0) this[0]-- }
                            }
                        )
                    }
                    if (barber.saloonType == "Female Salon" || barber.saloonType == "Hybrid Salon") {

                        GenderCounter(
                            genderName = "Female",
                            genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                            count = viewModel.genderCounter.value[1],
                            onIncrement = {
                                viewModel.genderCounter.value =
                                    viewModel.genderCounter.value.toMutableList()
                                        .apply { this[1]++ }
                            },
                            onDecrement = {
                                viewModel.genderCounter.value =
                                    viewModel.genderCounter.value.toMutableList()
                                        .apply { if (this[1] > 0) this[1]-- }
                            }
                        )
                    }
                    GenderCounter(
                        genderName = "Other",
                        genderImage = R.drawable.salon_app_logo,  // replace with your image resource
                        count = viewModel.genderCounter.value[2],
                        onIncrement = {
                            viewModel.genderCounter.value =
                                viewModel.genderCounter.value.toMutableList()
                                    .apply { this[2]++ }
                        },
                        onDecrement = {
                            viewModel.genderCounter.value =
                                viewModel.genderCounter.value.toMutableList()
                                    .apply { if (this[2] > 0) this[2]-- }
                        }
                    )
                    GeneralButton(
                        text = "Next",
                        width = 160,
                        height = 80,
                    ) {
                        if (viewModel.genderCounter.value.sum() > 0) {
                            if (viewModel.genderCounter.value.sum() <= 4) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "service",
                                    value = service
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "barber",
                                    value = barber
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "genders",
                                    value = viewModel.genderCounter.value
                                )
                                navController.navigate(Screens.serviceSelector.route)
                            } else {
                                showDialog = true
                                dialogMessage = "You can use this services for 4 people at a time"
                            }
                        } else {
                            showDialog = true
                            dialogMessage = "Please select your gender"
                        }
                    }
                }
            }
        }
    }
    AnimatedVisibility(showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Limit ") },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
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