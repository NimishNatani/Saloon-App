package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.components.ExpandableCard
import com.practicecoding.sallonapp.appui.components.ServiceNameAndPriceCard
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.ui.theme.sallonColor
import java.time.LocalDate

@Composable
fun DetailScreen(
    time: List<TimeSlot>,
    date: LocalDate,
    barber: BarberModel,
    service: List<Service>,
    genders: List<Int>, navController: NavController
) {
    BackHandler {
        navController.popBackStack()
    }
    val totalTime = service.sumOf { it.time.toInt() * it.count }
    val totalPrice = service.sumOf { it.price.toInt() * it.count }
    val scroll = rememberScrollState()

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
                            text = "${times.time}",
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
                    text = "${if (genders[0] > 0) "Male " else ""}${if (genders[1] > 0) "Female " else ""}${if (genders[2] > 0) "Other" else ""}",
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

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border( border = BorderStroke(1.dp, sallonColor),
                    shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 45.dp, vertical = 15.dp)) {
                Text(
                    text = "Cash",
                    color = sallonColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(15.dp))

            Box(modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(sallonColor)
                .padding(horizontal = 45.dp, vertical = 15.dp)) {
                Text(
                    text = "Online",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))



    }

}