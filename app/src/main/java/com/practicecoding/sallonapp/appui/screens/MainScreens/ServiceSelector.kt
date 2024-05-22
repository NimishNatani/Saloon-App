package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.ServiceAndPriceWithSelectCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ServiceSelector(
    navController: NavController, services: List<ServiceCat>,barber:BarberModel,
    viewModelBarber: GetBarberDataViewModel = hiltViewModel(), onBackClick: () -> Unit
) {
//    var services: List<ServiceCat> by initializeServices()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedServices by remember { mutableStateOf(emptyList<Service>()) }
    var isDialog by remember {
        mutableStateOf(false)
    }
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
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
                            border = BorderStroke(width = 0.5.dp, color = Color.Gray)
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
                        .background(Color.White)
                        .padding(top = 40.dp),
                    colors = CardColors(purple_200, purple_200, purple_200, purple_200),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, start = 15.dp, end = 15.dp)
                            .verticalScroll(scrollState)
                            .background(color = purple_200),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        services.forEach { services ->
                            ExpandableCard(title = services.type!!, expanded = true) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    services.services.forEach { service ->
                                        ServiceAndPriceWithSelectCard(
                                            serviceName = service.name!!,
                                            serviceTime = service.time,
                                            servicePrice = service.price,
                                            isServiceSelected = false
                                        ) { servicemain, isSelected ->
                                            if (isSelected) {
                                                /*List of services selected by the are stored here*/
                                                selectedServices = selectedServices + servicemain
                                            } else {
                                                selectedServices = selectedServices.filter { it.id != servicemain.id }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                        GeneralButton(text = "Next", width = 180, modifier = Modifier) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "services",
                                value = selectedServices

                            )
                             navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "barber",
                                value = barber

                            )
                            navController.navigate(Screens.DayTimeSelection.route)
                        }
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }

            }
        }
 //   }
}