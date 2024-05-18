package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    service: List<Service>
) {
    val totalTime = service.sumOf { it.time.toInt() }
    val totalPrice = service.sumOf { it.price.toInt() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Details", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.SemiBold,)
        Spacer(modifier = Modifier.height(10.dp))

        Card(colors = CardColors(Color.White, Color.White,  Color.White, Color.White),
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row( modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
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
        Card(colors = CardColors(Color.White, Color.White,  Color.White, Color.White),
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row (modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)){
                Text(
                    text = "Gender Type:",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Male",
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
                        servicePrice = service.price
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(15.dp),
        )
        Card(colors = CardColors(Color.White, Color.White,  Color.White, Color.White),
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row( modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text(
                    text = "Total Time:",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)


                )
                Text(
                    text ="$totalTime Minutes",
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



    }

}