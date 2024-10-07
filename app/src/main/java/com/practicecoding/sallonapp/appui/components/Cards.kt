package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingText
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.GetUserDataViewModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ReviewModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.purple_400
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingBottomTextCard(
    navController: NavController,
    onBoardingTextList: List<OnBoardingPageText>,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        border = BorderStroke(0.7.dp, Color.Gray),
        elevation = 8.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                .padding(top = 16.dp, start = 18.dp, end = 18.dp, bottom = 16.dp),
        ) {
            val pagerState = rememberPagerState(0, 0f) { onBoardingTextList.size }
            val scope = rememberCoroutineScope()
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter),
                state = pagerState,
                userScrollEnabled = false
            ) { text ->
                OnBoardingText(
                    mainHeading = onBoardingTextList[text].mainHeading,
                    bodyText = onBoardingTextList[text].bodyText,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                repeat(3) { index ->
                    DotIndicator(selected = index == pagerState.currentPage)
                }
            }
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.BottomEnd),
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(bottom = 32.dp, start = 32.dp)
                        .wrapContentSize(align = Alignment.BottomEnd)
                        .clip(RoundedCornerShape(50.dp)),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage > 0) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                } else {/*TODO*/
                                    //open the welcome screen
                                }
                            }
                            onBackClick()
                        }, modifier = Modifier.background(color = Color(sallonColor.toArgb()))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(bottom = 32.dp, end = 32.dp, start = 16.dp)
                        .wrapContentSize(align = Alignment.BottomEnd)
                        .clip(RoundedCornerShape(50.dp)),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage < 3) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {/*TODO*/
                                    //open the welcome screen
                                }
                            }
                            onNextClick()
                        }, modifier = Modifier.background(color = Color(sallonColor.toArgb()))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
            }
        }
    }
}

//#1 ProfileWithNotification Card
@Composable
fun ProfileWithNotification(
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: GetUserDataViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val user = viewModel.user.value
    LaunchedEffect(key1 = true) {
        viewModel.getUser()
    }
    if (user.name=="") {
        ShimmerEffectProfile()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .wrapContentHeight(),
            color = purple_200,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onProfileClick() }) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model =user.imageUri
                        ),
                        contentDescription = "User Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.name!!,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
//                Image(
//                    painter = painterResource(id = R.drawable.notificationbell),
//                    contentDescription = "notification bell",
//                    modifier = Modifier
//                        .size(22.dp)
//                        .clickable { onNotificationClick() })
            }
        }
    }
}

//Offer Card
@Composable
fun OfferCard(
    detailText: String,
    percentOff: Int,
    iconImageId: Int,
    onExploreClick: () -> Unit,
    cardColor: Color
) {
    Surface(
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = cardColor,
        contentColor = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 4.dp,
        border = null
    ) {
        Row(
            modifier = Modifier.padding(top = 5.dp, start = 8.dp, end = 8.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 6.dp, top = 5.dp, start = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$percentOff%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "OFF",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                }
                Text(
                    text = detailText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    modifier = Modifier.padding(4.dp, bottom = 12.dp)
                )
                Button(
                    onClick = { onExploreClick() },
                    modifier = Modifier
                        .width(125.dp)
                        .height(35.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Explore",
                        color = colorResource(id = R.color.sallon_color),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = iconImageId),
                contentDescription = "Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                )
        }
    }
}

//BigCard for horizontal pager
@Composable
fun BigSaloonPreviewCard(
    shopName: String,
    address: String,
    rating: Double,
    noOfReviews: Int,
    imageUrl: String,
    onBookNowClick: () -> Unit,
    onHeartClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier,
    distance: Double,
    open: Boolean,
    width: Dp,
    height: Dp
) {

    Surface(
        modifier = modifier
            .width(width)
            .height(height)
            .padding(),
        tonalElevation = 16.dp,
        shadowElevation = 5.dp,
        color = purple_400,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height - 120.dp)
                    .background(Color.White)
            ) {


                Image(
                    painter = rememberAsyncImagePainter(
                        imageUrl
                    ), // Placeholder image
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.FillBounds
                )
                Image(
                    painter = painterResource(
                        id = if (open) {
                            R.drawable.open
                        } else {
                            R.drawable.close
                        }
                    ),
                    contentDescription = "Categories", // We don't need content description for images used as buttons
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .padding(6.dp)
                        .clickable { /* Your click action */ }
                )
                IconButton(
                    onClick = onHeartClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Star Icon",
                        tint = colorResource(id = R.color.sallon_color),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purple_400)
                    .padding(vertical = 4.dp, horizontal = 16.dp),
            ) {
                Text(
                    text = shopName, fontSize = 18.sp, maxLines = 1
                )
                Spacer(modifier = Modifier.weight(0.5f))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Location",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp, end = 1.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rating1),
                    contentDescription = "Star Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "${Math. round(rating * 10.0) / 10.0} ($noOfReviews reviews)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(0.75f))
                Button(
                    onClick = onBookNowClick,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.purple_200)
                    )
                ) {
                    Text(
                        text = "Book Now",
                        color = colorResource(id = R.color.sallon_color),
                    )
                }
            }
        }

    }
}

//
//Customer review card from customers
@Composable
fun CustomerReviewCard(
//    customerName: String,
//    reviewText: String,
//    rating: Double,
//    imageUrl: String,
//    time: String = "2 days ago"
    review :ReviewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        val currentTime = LocalDateTime.now()
        val reviewTime =
            LocalDateTime.parse(review.reviewTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val duration = Duration.between(reviewTime, currentTime)
        val timePassed = when {
            duration.toDays() > 0 -> "${duration.toDays()} days ago"
            duration.toHours() > 0 -> "${duration.toHours()} hours ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
            else -> "Just now"}
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    /*TODO profile picture*/
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row {
                        Text(
                            text = review.userName,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Text(
                            text = timePassed,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        repeat((Math. round(review.rating * 10.0) / 10.0).toInt()) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star Icon",
                                tint = Color.Yellow,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Text(
                text = review.reviewText,
                fontSize = 16.sp,
                maxLines = 3,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            )
        }

    }
}

//Small preview card
@Composable
fun SmallSaloonPreviewCard(
    shopName: String,
    imageUri: String,
    address: String,
    distance: Double,
    numberOfReviews: Int,
    rating: Double,
    onHeartClick: () -> Unit,
    onBookClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier, open: Boolean
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(bottom = 16.dp)
            .background(Color.White),
        tonalElevation = 8.dp,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(16)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .wrapContentSize(),
        ) {
            Box(
                modifier = Modifier.width(100.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        imageUri
                    ), // Placeholder image
                    contentDescription = "barberImage",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onHeartClick,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopStart)
                ) {
                    Surface(
                        shape = CircleShape, color = Color.White
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Heart Icon",
                            tint = colorResource(id = R.color.sallon_color),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                    }
                }
                Image(
                    painter = painterResource(
                        id = if (open) {
                            R.drawable.open
                        } else {
                            R.drawable.close
                        }
                    ),
                    contentDescription = "Categories", // We don't need content description for images used as buttons
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .align(Alignment.BottomEnd)
                        .clickable { /* Your click action */ }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
//                verticalArrangement = Arrangement.Top
            ) {
                Row {
                    Text(
                        text = shopName, fontSize = 18.sp, maxLines = 1
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
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
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.rating1),
                                contentDescription = "Star Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "${ Math. round(rating * 10.0) / 10.0}",
                                fontSize = 14.sp,
                            )
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = "($numberOfReviews)",
                                fontSize = 14.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.weight(0.9f))
                    Button(
                        onClick = onBookClick,
                        modifier = Modifier.padding(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_200)
                        )
                    ) {
                        Text(
                            text = "Book Now",
                            color = colorResource(id = R.color.sallon_color),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceNameAndPriceCard(
    serviceName: String,
    serviceTime: String,
    servicePrice: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = serviceName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = serviceTime + "mins",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "Rs. $servicePrice",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        if (count > 0) {
            Text(
                text = "×${count}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ServiceAndPriceWithSelectCard(
    service: Service,
    noOfGender: Int,
    onServiceSelectedChange: (Service) -> Unit,
    getBarberDataViewModel: GetBarberDataViewModel
) {
    // Remember the state of the service
    var selectedService by remember { mutableStateOf(service) }
    if (getBarberDataViewModel.showDialog.value){
        AlertDialogBox(
            getBarberDataViewModel.dialogMessage.value,
            onDismiss = {getBarberDataViewModel.showDialog.value=false},
            onClick = {getBarberDataViewModel.showDialog.value = false}
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedService.serviceName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Row(
//            modifier = Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${selectedService.time} mins",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = "Rs. ${selectedService.price}",
                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(end = 8.dp)
            )

            Counter(
                count = selectedService.count,
                onIncrement = {
                    if (selectedService.count == noOfGender) {
                        getBarberDataViewModel.showDialog.value = true
                        getBarberDataViewModel.dialogMessage.value = listOf("Service","You reach your max for this service")
                    } else {
                        // Update the count
                        selectedService = selectedService.copy(count = selectedService.count + 1)
                        // Notify the parent
                        onServiceSelectedChange(selectedService)
                    }
                },
                onDecrement = {
                    if (selectedService.count > 0) {
                        selectedService = selectedService.copy(count = selectedService.count - 1)
                        onServiceSelectedChange(selectedService)
                    }
                }
            )
        }
    }
}

@Composable
fun Counter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Text(
                text = count.toString(),
                fontSize = 16.sp,
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


@Composable
fun BookingScreenShopPreviewCard(
    bookingModel: BookingModel,
    onOpenClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(2.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color.White)
            .clickable(onClick = { onOpenClick() })
            .padding(8.dp)
            .padding(start = 5.dp, end = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f).height(40.dp)) {
            Text(
                text = bookingModel.barber.shopName.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
//                    .weight(0.1f)
//                    .padding(end = 8.dp)
            )

            }
            Button(
                onClick = { onOpenClick() },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(purple_200.toArgb()),
                    contentColor = Color(sallonColor.toArgb())
                )
            ) {
                Text(
                    text = "Book",
                    color = sallonColor, fontWeight = FontWeight.SemiBold, fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Star Icon",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = bookingModel.barber.shopStreetAddress+bookingModel.barber.city ,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.rating1),
                contentDescription = "Star Icon",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "${Math. round(bookingModel.barber.rating * 10.0) / 10.0} (${bookingModel.barber.noOfReviews} reviews)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.direction),
                contentDescription = "direction image",
                modifier = Modifier
                    .size(30.dp)

            )
            Text(
                text = "Directions",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
