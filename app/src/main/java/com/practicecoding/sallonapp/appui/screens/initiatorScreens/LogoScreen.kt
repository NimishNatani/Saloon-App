package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import kotlinx.coroutines.delay


@Composable
fun LogoScreen(
    navController: NavController,
    logoDurationMillis: Long = 2000L // Default duration of 2000 milliseconds (2 seconds)
){

val context = LocalContext.current
        LaunchedEffect(true) {
            delay(logoDurationMillis)
            val updatedCurrentUser = FirebaseAuth.getInstance().currentUser
            if (updatedCurrentUser == null) {
                navController.navigate(Screens.OnBoardingScreens.route){
                    popUpTo(Screens.Logo.route) {
                        inclusive = true
                    }
                }
            }else{
                navController.navigate(Screens.MainScreen.route){
                    popUpTo(Screens.Logo.route) {
                        inclusive = true
                    }
                }

            }

        }
    Surface(
        modifier = Modifier.fillMaxSize(),
            ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                color = colorResource(id = R.color.sallon_color)
            ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.salon_app_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .aspectRatio(1.0f).wrapContentSize().clip(shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoScreenPreview() {
    LogoScreen(navController = rememberNavController())
}
