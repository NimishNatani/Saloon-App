package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.light_grey
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun RowofDate(isSelected: Boolean,date:String,day:String,onClick: () -> Unit) {
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
Text(text = date,color = if(isSelected){
    Color.White}else{
    Color.Gray}, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top=2.dp))
        }
        Text(text = day, modifier = Modifier.align(Alignment.CenterHorizontally), color = if(isSelected){
            Color.Black}else{
            Color.Gray})
    }
}