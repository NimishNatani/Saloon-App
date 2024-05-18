package com.practicecoding.sallonapp.appui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.BottomAppNavigationBar
import com.practicecoding.sallonapp.appui.components.BottomNavItems
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.HeadingText
import com.practicecoding.sallonapp.appui.components.ProfileWithNotification
import com.practicecoding.sallonapp.appui.components.SalonCard
import com.practicecoding.sallonapp.appui.components.SearchBar
import com.practicecoding.sallonapp.appui.screens.MainScreens.BarberScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.DetailScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.GenderSelectOnBook
import com.practicecoding.sallonapp.appui.screens.MainScreens.ServiceSelector
import com.practicecoding.sallonapp.appui.screens.MainScreens.TimeSelection
import com.practicecoding.sallonapp.appui.screens.MainScreens.TimeSlot
import com.practicecoding.sallonapp.appui.screens.MainScreens.ViewAllScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.daySelection
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.MainScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OTPVerificationScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.PhoneNumberScreen
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCat
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import java.time.LocalDate
import java.time.LocalTime

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
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "Sign up"
                    )
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
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "OTP Verification"
                    )
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
                    MainScreen(
                        navController = navController,
                        likedBarberViewModel = LikedBarberViewModel(context)
                    )
                },
                topAppBar = {
                    ProfileWithNotification(

                        onProfileClick = { /*TODO*/ },
                        onNotificationClick = { /*TODO*/ },
                    )
                },
                bottomAppBar = {
BottomAppNavigationBar(
    onHomeClick = { /*TODO*/ },
    onLocationClick = { /*TODO*/ },
    onBookClick = { /*TODO*/ },
    onMessageClick = { /*TODO*/ },
    onProfileClick = { /*TODO*/ },
    modifier = Modifier
)
                }
            )
        }
        composable(Screens.BarberScreen.route) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            if (result != null) {
                BarberScreen(
                    onBackClick = { navController.popBackStack() }, // Assuming you want to navigate back when back is clicked
                    onLikeClick = { /* Handle like click */ },
                    onShareClick = { /* Handle share click */ },
                    barber = result,
                    navController = navController
                )
            }
//            else {
//                // Handle the case where result is null gracefully
//                // This could be displaying an error message or navigating back
//                navController.popBackStack() // Navigate back if data is not available
//            }
        }
        composable(Screens.GenderSelection.route) {
            val service =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<ServiceCat>>("service")
            val barber =
                navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            if (service != null&&barber!=null) {
                GenderSelectOnBook(
                    navController = navController,
                    service = service,
                    barber = barber
                )
            }
        }
        composable(Screens.ViewAllScreen.route) {
            val resultType =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("type")
                    .toString()
            val resultLocation =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("location")
                    .toString()
            ViewAllScreen(type = resultType, location = resultLocation)
        }
        composable(Screens.serviceSelector.route) {
            val services =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<ServiceCat>>("service")
           val barber =
                navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            if (services != null&&barber!=null)
                ServiceSelector(navController = navController, onBackClick = {}, services = services,barber=barber)
        }
        composable(Screens.DayTimeSelection.route) {
            val startTime = LocalTime.of(10, 0)
            val endTime = LocalTime.of(21, 0)
            val intervalMinutes = 30L
            val bookedTimes = listOf(
                LocalTime.of(11, 0),
                LocalTime.of(13, 30),
                LocalTime.of(15, 0)
            )
            val notAvailableTimes = listOf(
                LocalTime.of(12, 30),
                LocalTime.of(16, 0)
            )
            val currentDate = LocalDate.now()
            val services =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<Service>>("services")
            val barber =
                navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            if (services != null&&barber!=null) {
                val time = services.sumOf { it.time.toString().toInt() }
                var date by remember {
                    mutableStateOf<LocalDate>(currentDate)
                }
                DoubleCard(
                    midCarBody = { date = daySelection() },
                    navController,
                    topAppBar = {
                        BackButtonTopAppBar(
                            onBackClick = { /*TODO*/ },
                            title = "Select Date & Time"
                        )
                    },
                    bottomAppBar = {},
                    mainScreen = {
                        TimeSelection(
                            startTime,
                            endTime,
                            intervalMinutes,
                            bookedTimes,
                            notAvailableTimes,
                            time,
                            date,
                            navController,services,barber
                        )
                    })
            }
        }
        composable(Screens.Appointment.route) {
            val time =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<TimeSlot>>("time")
            val date =
                navController.previousBackStackEntry?.savedStateHandle?.get<LocalDate>("date")
            val barber =
                navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            val services =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<Service>>("services")
            if (time != null && date != null && barber != null && services != null) {
                DoubleCard(midCarBody = {
                    SalonCard(
                        shopName = barber.shopName!!,
                        imageUri = barber.imageUri!!,
                        address = barber.shopStreetAddress!!,
                        distance = barber.distance!!,
                    )
                }, navController, topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { /*TODO*/ },
                        title = "Your Appointment"
                    )
                }, bottomAppBar = {}, mainScreen = {DetailScreen(time,date,barber,services)})
            }
        }
    }
}
