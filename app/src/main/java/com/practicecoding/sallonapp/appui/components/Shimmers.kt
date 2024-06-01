package com.practicecoding.sallonapp.appui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.light_grey
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.purple_400
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun RowofDate(isSelected: Boolean, date: String, day: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(
            topStartPercent = 100,
            topEndPercent = 100,
            bottomEndPercent = 100,
            bottomStartPercent = 100
        ), modifier = Modifier.size(width = 40.dp, height = 75.dp), colors = CardColors
            (
            containerColor = if (isSelected) {
                Color.White
            } else {
                purple_200
            },
            contentColor = Color.Gray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        ),
        onClick = onClick

    ) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .size(32.dp), colors = CardColors(
                containerColor = if (isSelected) {
                    sallonColor
                } else {
                    light_grey
                },
                contentColor = light_grey,
                disabledContainerColor = light_grey,
                disabledContentColor = light_grey
            ),
            shape = RoundedCornerShape(size = 20.dp)
        ) {
            Text(
                text = date, color = if (isSelected) {
                    Color.White
                } else {
                    Color.Gray
                }, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
        }
        Text(
            text = day,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = if (isSelected) {
                Color.Black
            } else {
                Color.Gray
            }
        )
    }
}

@Composable
fun ShimmerEffectBarberBig(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp),
        tonalElevation = 14.dp,
        shadowElevation = 4.dp,
        color = purple_400,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = modifier
                .width(width)
                .height(height), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height - 120.dp)
                    .background(Color.White)
                    .shimmerEffect()
            ) {
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                Modifier
                    .padding(horizontal = 20.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .shimmerEffect()
                    .fillMaxWidth()
                    .height(30.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                Modifier
                    .padding(start = 20.dp, end = 40.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .shimmerEffect()
                    .fillMaxWidth()
                    .height(30.dp)
            ) {

            }

        }

    }
}

@Composable
fun ShimmerEffectBarberSmall() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 20.dp, horizontal = 20.dp),
        tonalElevation = 14.dp,
        shadowElevation = 4.dp,
        color = purple_400,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .background(Color.White)
                    .shimmerEffect()
            ) {

            }
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shimmerEffect()
                        .fillMaxWidth()
                        .height(20.dp)
                ) {

                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    Modifier
                        .padding(start = 20.dp, end = 40.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .shimmerEffect()
                        .fillMaxWidth()
                        .height(20.dp)
                ) {

                }
            }
        }

    }

}

@Composable
fun ShimmerEffectMainScreen() {
    val scrollStateRowOffer = rememberScrollState()
    val scroll = rememberScrollState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrollStateRowCategories = rememberScrollState()
    val scrollStateNearbySalon = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollStateRowOffer)
                    .padding(end = 18.dp)
            ) {
                for (i in 1 until 4) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        contentColor = Color.White,
                        modifier = Modifier
                            .width(300.dp)
                            .height(150.dp)
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                }


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Categories",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )

                )
                TextButton(onClick = {}) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            Row(modifier = Modifier.horizontalScroll(scrollStateRowCategories)) {
                for (i in 1 until 6) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(48.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 20.dp, height = 8.dp)
                                .clip(RoundedCornerShape(48.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nearby Salons",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )

                )
                TextButton(onClick = {
                }) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            Row(modifier = Modifier.horizontalScroll(scrollStateNearbySalon)) {
                for (i in 1 until 4) {


                    ShimmerEffectBarberBig(screenWidth - 50.dp, screenWidth - 85.dp)

                    Spacer(modifier = Modifier.width(10.dp))
                }


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Popular Saloon",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )

                )
                TextButton(onClick = {

                }) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            for (i in 1 until 4) {

                ShimmerEffectBarberSmall()
            }

        }
    }
}

@Composable
fun ShimmerEffectProfile() {
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
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(
                        shape = RoundedCornerShape(35.dp)
                    )
                    .shimmerEffect()
            ) {

            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

            }

            Spacer(modifier = Modifier.width(16.dp))
//            Image(
//                painter = painterResource(id = R.drawable.notificationbell),
//                contentDescription = "notification bell",
//                modifier = Modifier
//                    .size(22.dp)
//                    )
        }
    }
}

@Composable
fun ShimmerEffectBarber() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = purple_400
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            ) {

            }
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)),
                colors = CardColors(Color.White, Color.White, Color.White, Color.White)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .size(width = 200.dp, height = 45.dp)
                            .padding(start = 15.dp,end=20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 45.dp)
                            .padding(end=15.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(width = 150.dp, height = 30.dp)
                        .padding(start = 15.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 25.dp)
                        .padding(start = 15.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .size(width = 300.dp, height = 45.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }

        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transtion = rememberInfiniteTransition(label = "")
    val startOffsetX by transtion.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(2000)), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFD9D7DA),
                Color(0xFFC3C2C4),
//                Color(0xFFBEBCBC)
            ), start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned { size = it.size }
}