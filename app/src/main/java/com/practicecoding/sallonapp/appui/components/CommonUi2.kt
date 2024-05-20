package com.practicecoding.sallonapp.appui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.Purple80
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
fun ShimmerEffect(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    for (i in 1 until 3) {
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
                        .shimmerEffect()
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    Modifier
                        .padding(start = 20.dp, end = 40.dp)
                        .shimmerEffect()
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                }

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
                Purple80,
                purple_200, purple_400
            ), start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned { size = it.size }
}