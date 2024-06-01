package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.ServiceNameAndPriceCard
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    time: List<TimeSlot>,
    date: LocalDate,
    barber: BarberModel,
    service: List<Service>,
    genders: List<Int>,
    navController: NavController
) {
    BackHandler {
        navController.popBackStack()
    }

    var sheetStae by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var selectedPaymentMethod by remember { mutableStateOf("Pay Online") }

    val totalTime = service.sumOf { it.time.toInt() * it.count }
    val totalPrice = service.sumOf { it.price.toInt() * it.count }
    val scroll = rememberScrollState()

    ModalBottomSheetLayout(
        sheetState = if (!sheetStae) ModalBottomSheetState(ModalBottomSheetValue.Hidden) else ModalBottomSheetState(
            ModalBottomSheetValue.Expanded
        ),
        sheetContent = {
            PaymentMethodBottomSheet(totalPrice = totalPrice,
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentMethodSelect = { selectedPaymentMethod = it },
                onClose = { sheetStae = false })
        },
        sheetShape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp),
        sheetGesturesEnabled = true,
        sheetBackgroundColor = purple_200
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
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
                        text = "$date",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text(
                        text = "Time:",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,

                        )
                    Column {
                        for (times in time) {
                            Text(
                                text = "${times.time}  ",
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
                        text = "${if (genders[0] > 0) "Male  " else ""}${if (genders[1] > 0) "Female  " else ""}${if (genders[2] > 0) "Other" else ""}",
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
                    service.forEach { service ->
                        if (service.count > 0) {
                            ServiceNameAndPriceCard(
                                serviceName = service.serviceName,
                                serviceTime = service.time,
                                servicePrice = service.price,
                                count = service.count
                            )
                        }
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

            GeneralButton(text = "Pay Now", width = 300) {
                scope.launch { sheetStae = true }

            }
            Spacer(modifier = Modifier.height(15.dp))


        }
    }

}

@Composable
fun PaymentMethodBottomSheet(
    totalPrice: Int,
    selectedPaymentMethod: String,
    onPaymentMethodSelect: (String) -> Unit,
    onClose: () -> Unit
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

        Spacer(modifier = Modifier.height(26.dp))
        Card(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
            colors = CardColors(Color.White, Color.White, Color.White, Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp, start = 20.dp, end = 20.dp)
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
                        color = Color.Gray
                    )
                    GeneralButton(
                        text = if (selectedPaymentMethod == "Pay Cash") "Confirm Cash Payment" else "Proceed to Pay Online",
                        width = 200
                    ) {
                        // Handle payment action here
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