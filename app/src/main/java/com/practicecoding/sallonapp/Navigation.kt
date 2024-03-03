package com.practicecoding.sallonapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicecoding.sallonapp.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OTPVerificationScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.PhoneNumberScreen

@Composable
fun AppNavigation(
navController : NavHostController
) {
    NavHost(navController = navController, startDestination = Screens.Logo.route) {
        composable(Screens.Logo.route) {
            LogoScreen(navController = navController)
        }
        composable(Screens.OnBoardingScreens.route) {
            val imageList = listOf(
                R.drawable.onboarding1,
                R.drawable.onboarding2,
                R.drawable.onboarding3
            )
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
            OnBoardingScreen(
                navController = navController,
                imageList = imageList,
                OnBoardingTextList = onBoardingTextList
            )
        }
        composable(Screens.PhoneNumberScreen.route) {
            PhoneNumberScreen(
                navigateToVerification = {phoneNumber ->
                    navController.navigate(Screens.OTPVerification.route + "/$phoneNumber")
                }
            )
        }
        composable(Screens.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")?:"000"
            OTPVerificationScreen(phoneNumber)
        }
        composable(Screens.SignUp.route){
            AdvancedSignUpScreen()
        }
    }
}
