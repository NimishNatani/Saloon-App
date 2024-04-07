package com.practicecoding.sallonapp.appui.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.BottomAppNavigationBar
import com.practicecoding.sallonapp.appui.components.BottomNavItems
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.HeadingText
import com.practicecoding.sallonapp.appui.components.ProfileWithNotification
import com.practicecoding.sallonapp.appui.components.SearchBar
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.MainScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OTPVerificationScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.PhoneNumberScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Screens.Logo.route) {
        composable(Screens.Logo.route) {
            LogoScreen(navController = navController)
        }
        composable(
            Screens.OnBoardingScreens.route,
        ) {
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
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "Sign up to access all the feature of barber shop") },
                mainScreen = {
                    PhoneNumberScreen(activity = context as Activity,
                        navigateToVerification = { phoneNumber ->
                            navController.navigate(Screens.OTPVerification.route + "/$phoneNumber")
                        }
                    )
                },
                topAppBar = {
                    Column {
                        BackButtonTopAppBar(
                            onBackClick = { navController.popBackStack() },
                            title = "Sign up"
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            )
        }
        composable(Screens.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "000"
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "We have sent an otp to $phoneNumber") },
                mainScreen = {
                    OTPVerificationScreen(
                        phoneNumber = phoneNumber,
                        activity = context as Activity,
                        navController = navController,
                    )
                },
                topAppBar = {
                    Column {
                        BackButtonTopAppBar(
                            onBackClick = { navController.popBackStack() },
                            title = "OTP Verification"
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            )
        }
        composable(Screens.SignUp.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "000"
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "Enter your details to access all the feature of barber shop") },
                mainScreen = {
                    AdvancedSignUpScreen(
                        phoneNumber = phoneNumber,
                        activity = context as Activity,
                        navController=navController
                    )
                },
                topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "Sign Up"
                    )
                }
            )
        }
        composable(Screens.SignUp.route) {
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "Enter your details to access all the feature of barber shop") },
                mainScreen = {
                    AdvancedSignUpScreen(
                        phoneNumber = "000",
                        activity = context as Activity,
                        navController = navController
                    )
                },
                topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "Sign Up"
                    )
                }
            )
        }
        composable(Screens.MainScreen.route) {
            DoubleCard(midCarBody = { SearchBar() },
                mainScreen = {
                             MainScreen()
                },
                topAppBar = {
                    ProfileWithNotification(

                        onProfileClick = { /*TODO*/ },
                        onNotificationClick = { /*TODO*/ })
                },
                bottomAppBar = {
                    BottomAppNavigationBar(
                        currentScreen = BottomNavItems.Home,
                        onHomeClick = { /*TODO*/ },
                        onLocationClick = { /*TODO*/ },
                        onBookClick = { /*TODO*/ },
                        onMessageClick = { /*TODO*/ },
                        onProfileClick = { /*TODO*/ })
                }
                )
        }
    }
}
