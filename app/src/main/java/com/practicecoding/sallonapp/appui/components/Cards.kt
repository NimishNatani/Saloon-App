package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingText
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

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
    ){

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
                val pagerState = rememberPagerState(0,0f) {onBoardingTextList.size }
                val scope = rememberCoroutineScope()
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.TopCenter),
                state = pagerState,
                    userScrollEnabled = false
                ) {text ->
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
                        IconButton(onClick ={
                            scope.launch {
                                if(pagerState.currentPage > 0){
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }else{
                                    /*TODO*/
                                    //open the welcome screen
                                }
                            }
                            onBackClick()
                        },
                            modifier = Modifier.background(color =  Color(sallonColor.toArgb()))
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
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
                        IconButton(onClick ={
                            scope.launch {
                                if(pagerState.currentPage < 3){
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }else{
                                    /*TODO*/
                                    //open the welcome screen
                                    // navController.navigate("welcome")
                                }
                            }
                            onNextClick()
                        },
                            modifier = Modifier.background(color =  Color(sallonColor.toArgb()))
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
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


@Composable
fun DotIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(8.dp)
            .background(
                color = if (selected) Color(sallonColor.toArgb()) else Color(purple_200.toArgb()),
                shape = CircleShape
            )
    )
}
//@Preview(showBackground = true)
@Composable
fun OnBoardingBottomTextCardPreview(){
    val onBoardingTextList = listOf(
        OnBoardingPageText(
            mainHeading = "Heading 1",
            bodyText = "Body 1"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 2",
            bodyText = "Body 2"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 3",
            bodyText = "Body 3"
        ),
    )
    val navController = rememberNavController()
    OnBoardingBottomTextCard(
        navController = navController,
        onBoardingTextList = onBoardingTextList,
        onNextClick = {},
        onBackClick = {}
    )
}

@Composable
fun DoubleCard(
    title: String,
    onBackClick: () -> Unit,
    midCarBody: @Composable () -> Unit,
    navController: NavController = rememberNavController(),
    mainScreen: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.purple_200)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        topAppBar()
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            backgroundColor = colorResource(id = R.color.sallon_color)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                midCarBody()

                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                    backgroundColor = colorResource(id = R.color.white)
                ) {
                    mainScreen()
                }
            }
        }
    }
}

@Composable
fun HeadingText(
    bodyText: String
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = bodyText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

//#1 ProfileWithNotification Card
@Composable
fun ProfileWithNotification(
    name: String,
    address: String,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = Color.LightGray,
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onProfileClick() }
            ) {
                /*TODO profile picture*/
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Surface(
                shape = CircleShape,
                color = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNotificationClick() }
            ) {
                /*TODO notification icon*/
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
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = cardColor,
        contentColor = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 4.dp,
        border = null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ){
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$percentOff%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "OFF",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detailText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(250.dp)
                )
                Button(
                    onClick = { onExploreClick() },
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Explore",
                        color = colorResource(id = R.color.sallon_color),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = iconImageId),
                contentDescription = "Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .width(500.dp),

            )
        }
    }
}

//BigCard for horizontal pager
@Composable
fun BigSaloonPreviewCard(
    shopName: String,
    address: String,
    rating: Float,
    noOfReviews: Int,
    imageUrl: String,
    onBookNowClick: () -> Unit,
    onHeartClick: () -> Unit,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier,
    distance: Int
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_facebook), // Placeholder image
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onHeartClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Star Icon",
                            tint = colorResource(id = R.color.sallon_color),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row {
                        Text(
                            text = shopName,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Row {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Star Icon",
                                tint = colorResource(id = R.color.sallon_color),
                                modifier = Modifier.size(18.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Star Icon",
                                tint = colorResource(id = R.color.sallon_color),
                                modifier = Modifier.size(18.dp)
                            )

                            Text(
                                text = "${distance} Km",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.sallon_color)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Star Icon",
                            tint = Color.Green,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = address,
                            fontSize = 14.sp,
                            maxLines = 3,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$rating",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "Star Icon",
                            tint = Color.Yellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "(${noOfReviews})",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.weight(0.75f))
                        Button(
                            onClick = onBookNowClick,
                            modifier = Modifier.padding(16.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.grey_light)
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
}

//Small preview card
@Composable
fun SmallSaloonPreviewCard(
    shopName: String,
    imageUrl: String,
    address: String,
    distance: Int,
    numberOfReviews: Int,
    rating: Float,
    onBookClick: () -> Unit,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .wrapContentSize(),
        ) {
            Box(
                modifier = Modifier.wrapContentSize(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.salon_app_logo), // Placeholder image
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { /* Toggle favorite */ },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .align(Alignment.TopStart)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
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
            }
            Spacer(modifier = Modifier.width(8.dp))
           Column(
               modifier = Modifier
                   .padding(8.dp)
                   .fillMaxWidth(),
               verticalArrangement = Arrangement.Top
           ) {
               Row {
                   Text(
                       text = shopName,
                       fontSize = 18.sp,
                       maxLines = 1
                   )
                   Spacer(modifier = Modifier.weight(0.5f))
                   Row {
                       Icon(
                           imageVector = Icons.Default.ArrowBack,
                           contentDescription = "Star Icon",
                           tint = colorResource(id = R.color.sallon_color),
                           modifier = Modifier.size(18.dp)
                       )
                       Icon(
                           imageVector = Icons.Default.ArrowForward,
                           contentDescription = "Star Icon",
                           tint = colorResource(id = R.color.sallon_color),
                           modifier = Modifier.size(18.dp)
                       )

                       Text(
                           text = "${distance} Km",
                           fontSize = 14.sp,
                           color = colorResource(id = R.color.sallon_color)
                       )
                   }
               }
               Spacer(modifier = Modifier.height(4.dp))
               Row {
                   Icon(
                       imageVector = Icons.Default.LocationOn,
                       contentDescription = "Star Icon",
                       tint = Color.Green,
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
                   Text(
                       text = "$rating",
                       fontSize = 14.sp
                   )
                   Spacer(modifier = Modifier.width(4.dp))
                   Icon(
                       painter = painterResource(id = R.drawable.img),
                       contentDescription = "Star Icon",
                       tint = Color.Yellow,
                       modifier = Modifier.size(18.dp)
                   )
                   Text(
                       text = "(${numberOfReviews})",
                       fontSize = 14.sp,
                       color = Color.Gray
                   )
                   Spacer(modifier = Modifier.weight(0.9f))
                   Button(
                       onClick = onBookClick,
                       modifier = Modifier.padding(16.dp),
                       shape = RoundedCornerShape(50),
                       colors = ButtonDefaults.buttonColors(
                           containerColor = colorResource(id = R.color.grey_light)
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
fun BottomAppNavigationBar(
    currentScreen: BottomNavItems,
    onHomeClick: () -> Unit,
    onLocationClick: () -> Unit,
    onBookClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
){
    BottomAppBar(
        modifier = modifier.height(56.dp),
        contentPadding = PaddingValues(8.dp),
        containerColor = colorResource(id = R.color.white),
        tonalElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp, bottom = 2.dp)
                .clip(RoundedCornerShape(50)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButtonWithTriangle(
                onClick = onHomeClick,
                enabled = currentScreen != BottomNavItems.Home,
                isSelected = currentScreen == BottomNavItems.Home,
                icon = Icons.Default.Home
            )
            IconButtonWithTriangle(
                onClick = onLocationClick,
                enabled = currentScreen != BottomNavItems.Location,
                isSelected = currentScreen == BottomNavItems.Location,
                icon = Icons.Default.LocationOn
            )
            IconButtonWithTriangle(
                onClick = onBookClick,
                enabled = currentScreen != BottomNavItems.Book,
                isSelected = currentScreen == BottomNavItems.Book,
                icon = Icons.Default.ShoppingCart
            )
            IconButtonWithTriangle(
                onClick = onMessageClick,
                enabled = currentScreen != BottomNavItems.Message,
                isSelected = currentScreen == BottomNavItems.Message,
                icon = Icons.Default.Send
            )
            IconButtonWithTriangle(
                onClick = onProfileClick,
                enabled = currentScreen != BottomNavItems.Profile,
                isSelected = currentScreen == BottomNavItems.Profile,
                icon = Icons.Default.Person
            )
        }
    }
}

@Composable
private fun IconButtonWithTriangle(
    onClick: () -> Unit,
    enabled: Boolean,
    isSelected: Boolean,
    icon: ImageVector
) {
    Column(
        modifier = Modifier.clickable(enabled = enabled) { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurvyTriangleWithCircle(
                color = if (isSelected) Color(sallonColor.toArgb()) else Color.Transparent,
                alignment = Alignment.TopStart
            )

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) colorResource(id = R.color.sallon_color) else Color.Gray,
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
private fun CurvyTriangleWithCircle(
    color: Color,
    alignment: Alignment
) {
    Box(
        modifier = Modifier.size(16.dp),
        contentAlignment = alignment
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width / 2, size.height)
                    lineTo(0f, 0f)
                    close()
                },
                color = color
            )
        }
    }
}

enum class BottomNavItems {
    Home, Location, Book, Message, Profile
}

@Preview(showBackground = false)
@Composable
fun CardsPreview() {
    BottomAppNavigationBar(
        currentScreen = BottomNavItems.Profile,
        onHomeClick = {},
        onLocationClick = {},
        onBookClick = {},
        onMessageClick = {},
        onProfileClick = {}
    )
}