package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun GeneralButton(text:String,width:Int,height:Int,modifier: Modifier,onClick:()->Unit){
    Box (

        modifier = Modifier
            .width(width.dp)
            .height(80.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .background(color = Color(sallonColor.toArgb()),shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
    ) {
        Text(text= text, color = Color.White)
    }
}