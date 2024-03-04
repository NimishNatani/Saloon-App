package com.practicecoding.sallonapp.appui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.screens.DoubleCard
import com.practicecoding.sallonapp.appui.screens.HeadingText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OTPVerificationScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.PhoneNumberScreen

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
            DoubleCard(title = "Sign up",{
                                         navController.popBackStack()
            }, body = {
                HeadingText(bodyText = "Sign up to access all the feature of barber shop")
            }) {
                PhoneNumberScreen(
                    navigateToVerification = {phoneNumber ->
                        navController.navigate(Screens.OTPVerification.route + "/$phoneNumber")
                    }
                )
            }
        }
        composable(Screens.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")?:"000"
            DoubleCard(title = "OTP verification",{
                navController.popBackStack()
            }, body =
            {
                HeadingText(bodyText = "We've send the code to your phone number $phoneNumber")
            }
            ) {
                OTPVerificationScreen(phoneNumber)
            }
        }
        composable(Screens.SignUp.route){
            DoubleCard(title = "Sign Up",{
                navController.popBackStack()
            }, body = {
                HeadingText(bodyText = "Sign up to access all the feature of barber shop")
            }) {
                AdvancedSignUpScreen()
            }
        }
    }
}
