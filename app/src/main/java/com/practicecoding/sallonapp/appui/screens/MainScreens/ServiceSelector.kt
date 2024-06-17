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
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.ServiceAndPriceWithSelectCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.ui.theme.purple_200

@Composable
fun ServiceSelector(
    navController: NavController,
    services: List<ServiceCat>, barber: BarberModel,
    genders: List<Int>,
    viewModel: GetBarberDataViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    BackHandler {
        navController.popBackStack()
    }
//    var services: List<ServiceCat> by initializeServices()
    val scrollState = rememberScrollState()
    val noOfGender by remember {
        mutableIntStateOf(genders.sum())
    }
    LaunchedEffect(Unit) {
        viewModel.initializedServices(services)
    }
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(purple_200), color = Color.White) {
        Box(modifier = Modifier.fillMaxSize().background(purple_200)){
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
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purple_200)
                    .padding(top = 40.dp),
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
                    services.forEach { serviceCat ->
                        ExpandableCard(title = serviceCat.type!!, expanded = true) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                serviceCat.services.forEach { serviceModel ->
                                    val service =
                                        viewModel.listOfService.value.find { it.id == serviceModel.name }
                                    if (service != null) {
                                        ServiceAndPriceWithSelectCard(
                                            service = service,
                                            noOfGender = noOfGender,
                                            onServiceSelectedChange = { updatedService ->
                                                // Update the list of selected services here
                                                viewModel.updateService(updatedService)
                                            }
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
            GeneralButton(text = "Next", width = 180, modifier = Modifier.align(Alignment.BottomCenter)) {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "services",
                    value = viewModel.listOfService.value

                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "barber",
                    value = barber

                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "genders",
                    value = genders

                )
                navController.navigate(Screens.DayTimeSelection.route)
            }
    }
       }
}