package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.AlertDialogBox
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.ServiceAndPriceWithSelectCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.ui.theme.purple_200

@Composable
fun ServiceSelector(
    navController: NavController,
    bookingModel: BookingModel,
    getBarberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    BackHandler {
        navController.popBackStack()
    }
    if (getBarberDataViewModel.showDialog.value){
        AlertDialogBox(
            getBarberDataViewModel.dialogMessage.value,
            onDismiss = {getBarberDataViewModel.showDialog.value=false},
            onClick = {getBarberDataViewModel.showDialog.value = false}
        )
    }
//    var services: List<ServiceCat> by initializeServices()
    val scrollState = rememberScrollState()
    val noOfGender by remember {
        mutableIntStateOf(bookingModel.genderCounter.sum())
    }
    LaunchedEffect(Unit) {
        getBarberDataViewModel.initializedServices(bookingModel.services)
    }

    val listOfServices by getBarberDataViewModel.listOfService.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(purple_200), color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(purple_200)
        ) {
            Column(
                modifier = Modifier
                    .background(purple_200)
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
                        text = "ServiceSelection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
//            BannerAds(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(purple_200)
                        .padding(top = 25.dp),
                    colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, start = 15.dp, end = 15.dp, bottom = 65.dp)
                            .verticalScroll(scrollState),
//                        .background(color = Color.White),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        bookingModel.services.forEach { serviceCategory ->
                            ExpandableCard(title = serviceCategory.type!!, expanded = true) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    serviceCategory.services.forEach { serviceModel ->
                                        val service =
                                            listOfServices.find { it.id == serviceModel.name }
                                        if (service != null) {
                                            ServiceAndPriceWithSelectCard(
                                                service = service,
                                                noOfGender = noOfGender,
                                                onServiceSelectedChange = { updatedService ->
                                                    // Update the list of selected services here
                                                    getBarberDataViewModel.updateService(
                                                        updatedService
                                                    )
                                                },
                                                getBarberDataViewModel
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                        Spacer(modifier = Modifier.height(40.dp))

                    }
                }

            }
            GeneralButton(
                text = "Next",
                width = 180,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                if (listOfServices.none { it.count >= 1 }) {
                    getBarberDataViewModel.showDialog.value = true
                    getBarberDataViewModel.dialogMessage.value =
                        listOf("Service Selection", "Please select some service")
                } else {
                    bookingModel.listOfService = listOfServices.filter { it.count >= 1 }
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "bookingModel",
                        value = bookingModel
                    )
                    navController.navigate(Screens.DayTimeSelection.route)
                }
            }
        }
    }
}