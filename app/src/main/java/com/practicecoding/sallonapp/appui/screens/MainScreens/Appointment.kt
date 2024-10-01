package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.ServiceNameAndPriceCard
import com.practicecoding.sallonapp.appui.components.SuccessfulDialog
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MainEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    bookingModel: BookingModel,
    navController: NavController,
    messageViewModel: MessageViewModel = hiltViewModel(),
    getBarberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
) {
    BackHandler {
        navController.popBackStack()
    }

    var sheetState by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var selectedPaymentMethod by remember { mutableStateOf("Pay Online") }

    val totalTime = bookingModel.listOfService.sumOf { it.time.toInt() * it.count }
    val totalPrice = bookingModel.listOfService.sumOf { it.price.toInt() * it.count }
    val scroll = rememberScrollState()

    var isdialog by remember {
        mutableStateOf(false)
    }
    if (isdialog) {
        SuccessfulDialog(navController = navController)
    }
    ModalBottomSheetLayout(
        sheetState = if (!sheetState) ModalBottomSheetState(ModalBottomSheetValue.Hidden) else ModalBottomSheetState(
            ModalBottomSheetValue.Expanded
        ),
        sheetContent = {
            PaymentMethodBottomSheet(totalPrice = totalPrice,
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentMethodSelect = { selectedPaymentMethod = it },
                onClose = { sheetState = false }, navController = navController,
                onCashClick = {
                    scope.launch(Dispatchers.IO) {
                        getBarberDataViewModel.onEvent(
                            MainEvent.setBooking(
                                bookingModel
                            )
                        )
                        val currentDate = Date()
                        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        val formattedDate = dateFormat.format(currentDate)
                        val message = LastMessage(
                            status = false,
                            message = "Hello Sir/Madam How may i Help you",
                            time = formattedDate,
                            seenbybarber = true,
                            seenbyuser = false
                        )
                        messageViewModel.onEvent(MessageEvent.AddChat(message, bookingModel.barber.uid))
                    }
                    isdialog = true
                }, onOnlineClick = {})
        },
        sheetShape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp),
        sheetGesturesEnabled = true,
        sheetBackgroundColor = purple_200
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 70.dp)
                    .verticalScroll(scroll),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Details",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = "Date:",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,

                            )
                        Text(
                            text = "${bookingModel.selectedDate}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = "Time Slots:",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,

                            )
                        Column {
                            for (times in bookingModel.selectedSlots) {
                                Text(
                                    text = times.time,
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Card(
                    colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = "Gender Type:",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${if (bookingModel.genderCounter[0] > 0) "Male  " else ""}${if (bookingModel.genderCounter[1] > 0) "Female  " else ""}${if (bookingModel.genderCounter[2] > 0) "Other" else ""}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

                ExpandableCard(title = "Service List", expanded = true) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        bookingModel.listOfService.forEach { service ->

                                ServiceNameAndPriceCard(
                                    serviceName = service.serviceName,
                                    serviceTime = service.time,
                                    servicePrice = service.price,
                                    count = service.count
                                )

                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(15.dp),
                )
                Card(
                    colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = "Total Time:",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)


                        )
                        Text(
                            text = "$totalTime Minutes",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        Text(
                            text = "Total Price:",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Rs.$totalPrice",
                            color = sallonColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )


                    }
                }
                Spacer(modifier = Modifier.height(35.dp))


                Spacer(modifier = Modifier.height(15.dp))


            }
            GeneralButton(
                text = "Pay Now",
                width = 300,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                scope.launch { sheetState = true }

            }
        }
    }

}

@Composable
fun PaymentMethodBottomSheet(
    totalPrice: Int,
    selectedPaymentMethod: String,
    onPaymentMethodSelect: (String) -> Unit,
    onClose: () -> Unit,
    onCashClick: () -> Unit,
    onOnlineClick: () -> Unit,
    navController: NavController,
) {

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessHigh
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment Method", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                )
                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.clickable { onClose() })
            }

            Spacer(modifier = Modifier.height(20.dp))
            Card(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
                colors = CardColors(Color.White, Color.White, Color.White, Color.White)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                        .border(2.dp, color = sallonColor, shape = RoundedCornerShape(25.dp))
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)


                ) {


                    Text(
                        text = "Select Payment Method",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    PaymentOption(text = "Pay Online",
                        isSelected = selectedPaymentMethod == "Pay Online",
                        onSelect = { onPaymentMethodSelect("Pay Online") })

                    Spacer(modifier = Modifier.height(8.dp))

                    PaymentOption(text = "Pay Cash",
                        isSelected = selectedPaymentMethod == "Pay Cash",
                        onSelect = { onPaymentMethodSelect("Pay Cash") })

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total:\nRs.$totalPrice",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(15.dp)
                        )
                        GeneralButton(
                            text = if (selectedPaymentMethod == "Pay Cash") "Confirm Cash Payment" else "Proceed to Pay Online",
                            width = 200
                        ) {
                            if (selectedPaymentMethod == "Pay Cash") {
                                onCashClick()
                            } else onOnlineClick()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    val borderColor = if (isSelected) sallonColor else Color.Transparent
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(8.dp))
        .clickable { onSelect() }
        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = isSelected, onClick = onSelect, colors = RadioButtonColors(
                selectedColor = sallonColor,
                unselectedColor = Color.Gray,
                disabledSelectedColor = Color.Gray,
                disabledUnselectedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}