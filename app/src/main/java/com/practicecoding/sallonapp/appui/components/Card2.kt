package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.ui.theme.Purple80
import com.practicecoding.sallonapp.ui.theme.sallonColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState


@Composable
fun SalonCard(
    shopName: String,
    imageUri: String,
    address: String,
    distance: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp), tonalElevation = 8.dp,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(16)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier.width(80.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        imageUri
                    ), // Placeholder image
                    contentDescription = "barberImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
//                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = shopName, fontSize = 18.sp, maxLines = 1
                )


                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Star Icon",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = address,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.width(200.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.distance),
                        contentDescription = "Diastance Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 6.dp)
                    )
                    Text(
                        text = "${distance} Km",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.sallon_color)
                    )
                }
            }
        }
    }
}
@Composable
fun OrderCard(
    imageUrl: String,
    orderType: List<String>,
    timeSlot: List<String>,
    phoneNumber: String,
    barbershopName: String,
    barberName: String,
    paymentMethod: String = "Cash",
    onRequestCancel: () -> Unit,
    onCancel: () -> Unit,
    onContactBarber: () -> Unit,
    onReview: () -> Unit,
    orderStatus: OrderStatus
) {
    val showInfoDialog = rememberMaterialDialogState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Card(
        shape = RoundedCornerShape(12),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = CardColors(
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White,
            containerColor = Color.White
        ),
        border = BorderStroke(1.2.dp, Color(sallonColor.toArgb()))
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = barberName,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = {
                                    showInfoDialog.show()
                                },
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(sallonColor.toArgb())
                                )
                            }
                        }
                        Text(
                            text = "Order: ${orderType.joinToString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Time Slot: ${timeSlot.joinToString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Payment Method: $paymentMethod",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(orderStatus == OrderStatus.COMPLETED || orderStatus == OrderStatus.CANCELLED) {
                            if(orderStatus == OrderStatus.COMPLETED){
                                Text("Completed", color = Color.Green)
                                Button(
                                    modifier = Modifier.padding(start = 8.dp),
                                    onClick = {
                                        onReview()
                                              },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(Purple80.toArgb())
                                    )
                                ) {
                                    Text("Review", color = Color.Black)
                                }
                            }else{
                                Text("Cancelled", color = Color.Red,
                                    modifier = Modifier.padding(8.dp))
                            }
                        }else{
                            if (orderStatus == OrderStatus.PENDING) {
                                Button(
                                    onClick = onCancel,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(Purple80.toArgb())
                                    ),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Cancel", color = Color.Black)
                                }
                            } else if (orderStatus == OrderStatus.ACCEPTED) {
                                Button(
                                    onClick = onRequestCancel,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(Purple80.toArgb())
                                    ),
                                    modifier = Modifier.padding(end = 8.dp),
                                ) {
                                    Text("Request Cancellation", color = Color.Black)
                                }
                            } else if (orderStatus == OrderStatus.PENDING_CANCELLATION) {
                                Text("Pending Cancellation", color = Color(sallonColor.toArgb()))
                            }
                            Button(
                                modifier = Modifier.padding(start = 8.dp),
                                onClick = onContactBarber,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(Purple80.toArgb())
                                )
                            ) {
                                Text("Contact $barberName", color = Color.Black)
                            }
                        }
                    }
                MaterialDialog(
                    dialogState = showInfoDialog,
                    buttons = {
                        positiveButton(text = "OK") { showInfoDialog.hide() }
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text ="Barber: $barberName at $barbershopName")
                        Text("Phone Number: $phoneNumber")
                        Text(text = "Order Type: ${orderType.joinToString()}")
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingOrderCard(
    upcomingOrder: MutableState<OrderModel>,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
//        Text(text = "Upcoming order date: ${upcomingOrder.value.date}")
//        Text(text = "Upcoming order time: ${upcomingOrder.value.timeSlot[0]}")
    }
}

@Preview
@Composable
fun OrderCardPreview() {
    OrderCard(
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/profile_image%2FptNTxkdC31NBaSQbJT5cnQQHO2u2.jpg?alt=media&token=ed411c18-99ad-4db2-94ab-23e1c9b2b1b6",
        orderType = listOf("Haircut", "Shave"),
        timeSlot = listOf("10:00 AM", "11:00 AM"),
        phoneNumber = "1234567890",
        barbershopName = "Barber Shop",
        barberName = "Barber",
        paymentMethod = "Cash",
        onRequestCancel = {},
        onCancel = {},
        onContactBarber = {},
        onReview = {},
        orderStatus = OrderStatus.PENDING
    )
}