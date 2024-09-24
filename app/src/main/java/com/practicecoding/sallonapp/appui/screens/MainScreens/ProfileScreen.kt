package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.viewmodel.AuthViewModel
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.GetUserDataViewModel

@Composable
fun ProfileScreen(
    viewModel: GetBarberDataViewModel,
    navController: NavController
){
    DoubleCard(midCarBody = { PhotoWithName() }, mainScreen = { ProfileScreenList(navController) }, topAppBar = {
        Text(
            text = "Profile",
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    })
}
@Composable
fun ProfileScreenList(navController: NavController){
    val profileList = listOf(
        Pair( R.drawable.salon_app_logo,"My Profile"),
        Pair( R.drawable.salon_app_logo,"My Booking History"),
        Pair( R.drawable.salon_app_logo,"Favorite's Saloon"),
        Pair( R.drawable.salon_app_logo,"Privacy Policy"),
        Pair( R.drawable.salon_app_logo,"About Us"),
        Pair( R.drawable.salon_app_logo,"Log Out"),
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(22.dp)
        .verticalScroll(rememberScrollState())) {

          ShowingList(image = R.drawable.salon_app_logo, text = "Edit Profile"){
              navController.navigate(Screens.UpdateProfileScreen.route)
          }
          ShowingList(image = R.drawable.salon_app_logo, text ="My Booking History"){
                navController.navigate(Screens.BookingHistory.route)
          }
          ShowingList(image = R.drawable.salon_app_logo, text = "Favorite's Saloon"){
                navController.navigate(Screens.FavBarberList.route)
          }
          ShowingList(image = R.drawable.salon_app_logo, text = "Privacy Policy"){}
          ShowingList(image = R.drawable.salon_app_logo, text ="About Us"){}
          ShowingList(image = R.drawable.salon_app_logo, text ="Log Out"){}


    }
}

@Composable
fun PhotoWithName(viewModel: GetUserDataViewModel= hiltViewModel(),){
    Column(modifier = Modifier
       , horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter =  rememberAsyncImagePainter(
            model =viewModel.user.value.imageUri
        ), contentDescription ="userImage", modifier = Modifier
            .size(100.dp)
            .clip(CircleShape) ,
            contentScale = ContentScale.FillBounds)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = viewModel.user.value.name.toString(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun ShowingList(image:Int,text:String,onClick:()->Unit){
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically){
        Image(painter = painterResource(id =  image), contentDescription = text, modifier = Modifier
            .size(40.dp)
            .clip(CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text=text, color = Color.Black)
    }
    Spacer(modifier = Modifier.height(6.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(6.dp))
}