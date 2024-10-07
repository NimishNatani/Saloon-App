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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
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
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.ReviewModel
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
    onCancel: () -> Unit,
    onContactBarber: () -> Unit,
    onReview: () -> Unit,
    order: OrderModel
) {
    val showInfoDialog = rememberMaterialDialogState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val totalTime = order.listOfService.sumOf { it.time.toInt() * it.count }
    val totalPrice = order.listOfService.sumOf { it.price.toInt() * it.count }
    Card(
        shape = RoundedCornerShape(12),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                        painter = rememberAsyncImagePainter(model = order.imageUrl),
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
                                text = order.barberShopName,
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
                            text = "Order: ${order.listOfService.joinToString { it.serviceName }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Date: ${order.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Time Slot: ${order.timeSlot.joinToString { it.time }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Payment Method: ${order.paymentMethod}",
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
                    if (order.orderStatus == OrderStatus.COMPLETED || order.orderStatus == OrderStatus.CANCELLED) {
                        if (order.orderStatus == OrderStatus.COMPLETED) {
//                                val review = orderViewModel.getReviewByOrderId(orderID)?:ReviewModel()
                            if (order.review.reviewText == "" && order.review.rating == 0.0) {
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
                            } else {
                                ReviewText(review = order.review)
                            }
                        } else {
                            Text(
                                "Cancelled", color = Color.Red,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        if (order.orderStatus == OrderStatus.PENDING) {
                            Button(
                                onClick = onCancel,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(Purple80.toArgb())
                                ),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Cancel", color = Color.Black)
                            }
                        } else if (order.orderStatus == OrderStatus.ACCEPTED) {
                            Text(
                                "Accepted", color = Color.Green,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
//                            Button(
//                                modifier = Modifier.padding(start = 8.dp),
//                                onClick = onContactBarber,
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(Purple80.toArgb())
//                                )
//                            ) {
//                                Text("Contact $barberName", color = Color.Black)
//                            }
                    }
                }
                MaterialDialog(
                    dialogState = showInfoDialog,
                    buttons = {
                        positiveButton(text = "OK") { showInfoDialog.hide() }
                    },
                    border = BorderStroke(1.dp, sallonColor),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                                androidx.compose.material3.Text(
                                    text = "Date:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,

                                    )
                                androidx.compose.material3.Text(
                                    text = order.date,
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

                                Text(
                                    text = order.timeSlot.joinToString { it.time },
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                            }

                        }
                        Spacer(modifier = Modifier.height(5.dp))
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
                                    text = "${if (order.genderCounter[0] > 0) "Male  " else ""}${if (order.genderCounter[1] > 0) "Female  " else ""}${if (order.genderCounter[2] > 0) "Other" else ""}",
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
                                order.listOfService.forEach { service ->

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
                            modifier = Modifier.height(5.dp),
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
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewText(
    review: ReviewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        RatingBar(onRatingChanged = { }, rating = review.rating)
        Text(text = review.reviewText, color = sallonColor, )
    }
}

@Composable
fun UpcomingFeaturesCard(
) {
    Card(
        shape = RoundedCornerShape(12),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardColors(
            contentColor = Color(sallonColor.toArgb()),
            disabledContainerColor = Color(sallonColor.toArgb()),
            disabledContentColor = Color(sallonColor.toArgb()),
            containerColor = Color(sallonColor.toArgb())
        ),
        border = BorderStroke(0.2.dp, Color(sallonColor.toArgb()))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Upcoming Features",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "We are working on some amazing features to make your experience better. Stay tuned for more updates.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSalonCard() {
    UpcomingFeaturesCard()
}