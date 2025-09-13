package com.practicecoding.sallonapp.appui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun SaloonColorText(text:String,textSize:Int=20){
    Text(text=text, style = TextStyle(color= sallonColor), fontSize = textSize.sp)
}
@Composable
fun WhiteColorText(text:String,textSize:Int){
    Text(text=text, color = Color.White, fontSize = textSize.sp)
}