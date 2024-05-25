package com.practicecoding.sallonapp.appui.navigation

import android.app.Activity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.HeadingText
import com.practicecoding.sallonapp.appui.components.ProfileWithNotification
import com.practicecoding.sallonapp.appui.components.SalonCard
import com.practicecoding.sallonapp.appui.components.SearchBar
import com.practicecoding.sallonapp.appui.screens.MainScreens.BarberScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.BottomSheet
import com.practicecoding.sallonapp.appui.screens.MainScreens.DetailScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.MainScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.ServiceSelector
import com.practicecoding.sallonapp.appui.screens.MainScreens.SortBarber
import com.practicecoding.sallonapp.appui.screens.MainScreens.TimeSelection
import com.practicecoding.sallonapp.appui.screens.MainScreens.TimeSlot
import com.practicecoding.sallonapp.appui.screens.MainScreens.ViewAllScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.daySelection
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.LogoScreen
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
    navController: NavHostController,
    startDestinations: String
) {
    val enterTransition =
        slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val exitTransition =
        slideOutHorizontally(
            targetOffsetX = { -1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val popEnterTransition =
        slideInHorizontally(
            initialOffsetX = { -1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val popExitTransition =
        slideOutHorizontally(
            targetOffsetX = { 1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val context = LocalContext.current
    NavHost(navController = navController, startDestination = startDestinations) {
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
        composable(Screens.MainScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
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
                        onBookClick = { /*TODO*/ },
                        onMessageClick = { /*TODO*/ },
                        onProfileClick = { /*TODO*/ },
                        modifier = Modifier
                    )
                }
            )
        }
        composable(Screens.ViewAllScreen.route,enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val resultType =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("type")
            val resultCity =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("location")
            val resultLat =
                navController.previousBackStackEntry?.savedStateHandle?.get<Double>("latitude")
            val resultLong =
                navController.previousBackStackEntry?.savedStateHandle?.get<Double>("longitude")
            if (resultLat != null && resultLong != null && resultCity != null && resultType != null) {
                var isBottomBar by remember {
                    mutableStateOf(false)
                }
                var sortType by remember {
                    mutableStateOf(if (resultType == "NearBy") "Distance" else "Rating")
                }
                DoubleCard(
                    midCarBody = { SortBarber(onSortClick = { isBottomBar = true }) },
                    navController,
                    mainScreen = {
                        ViewAllScreen(
                            type = resultType,
                            location = resultCity,
                            latitude = resultLat,
                            longitude = resultLong,
                            likedBarberViewModel = LikedBarberViewModel(context),
                            navController = navController,
                            sortType = sortType

                        )
                    },
                    topAppBar = {
                        BackButtonTopAppBar(
                            onBackClick = { /*TODO*/ },
                            title = "$resultType Salon"
                        )
                    },
                    bottomAppBar = {if(isBottomBar) {
                        BottomSheet(
                            onDismiss = { isBottomBar = false },
                            initialSortType = sortType,
                            onSortTypeChange = { newSortType ->
                                sortType = newSortType
                            }
                        )
                    }})
            }
        }
        composable(Screens.BarberScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            if (result != null) {
                BarberScreen(
                    onBackClick = { navController.popBackStack() },
                    onLikeClick = { /* Handle like click */ },
                    onShareClick = { /* Handle share click */ },
                    barber = result,
                    navController = navController
                )
            }
        }
        composable(Screens.GenderSelection.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val resultType = navController.previousBackStackEntry?.savedStateHandle?.get<String>("type")
            val resultCity = navController.previousBackStackEntry?.savedStateHandle?.get<String>("location")
            val resultLat = navController.previousBackStackEntry?.savedStateHandle?.get<Double>("latitude")
            val resultLong = navController.previousBackStackEntry?.savedStateHandle?.get<Double>("longitude")
            if (resultLat != null && resultLong != null && resultCity != null && resultType != null) {
                var isBottomBar by remember {
                    mutableStateOf(false)
                }
                var sortType by remember {
                    mutableStateOf(if (resultType == "NearBy") "Distance" else "Rating")
                }
                DoubleCard(
                    midCarBody = { SortBarber(onSortClick = { isBottomBar = true }) },
                    navController,
                    mainScreen = {
                        ViewAllScreen(
                            type = resultType,
                            location = resultCity,
                            latitude = resultLat,
                            longitude = resultLong,
                            likedBarberViewModel = LikedBarberViewModel(context),
                            navController = navController,
                            sortType = sortType

                        )
                    },
                    topAppBar = {
                        BackButtonTopAppBar(
                            onBackClick = { navController.popBackStack() },
                            title = "$resultType Salon"
                        )
                    },
                    bottomAppBar = {
                        if (isBottomBar) {
                            BottomSheet(
                                onDismiss = { isBottomBar = false },
                                initialSortType = sortType,
                                onSortTypeChange = { newSortType ->
                                    sortType = newSortType
                                }
                            )
                        }
                    })
            }
        }
        composable(Screens.serviceSelector.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val services = navController.previousBackStackEntry?.savedStateHandle?.get<List<ServiceCat>>("service")
            val barber = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            val numberOfGenders = navController.previousBackStackEntry?.savedStateHandle?.get<List<Int>>("genders")
            if (services != null && barber != null && numberOfGenders != null)
                ServiceSelector(
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    services = services,
                    barber = barber,
                    genders = numberOfGenders
                )
        }
        composable(Screens.DayTimeSelection.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
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
            val services = navController.previousBackStackEntry?.savedStateHandle?.get<List<Service>>("services")
            val barber = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            val genders = navController.previousBackStackEntry?.savedStateHandle?.get<List<Int>>("genders")
            if (services != null && barber != null && genders != null) {
                val time = services.sumOf { it.time.toInt() * it.count }
                var date by remember {
                    mutableStateOf<LocalDate>(currentDate)
                }
                DoubleCard(
                    midCarBody = { date = daySelection() },
                    navController,
                    topAppBar = {
                        BackButtonTopAppBar(
                            onBackClick = { navController.popBackStack() },
                            title = "Select Date & Time"
                        )
                    },
                    bottomAppBar = { },
                    mainScreen = {
                        TimeSelection(startTime, endTime, intervalMinutes, bookedTimes, notAvailableTimes, time, date, navController, services, barber, genders
                        )
                    })
            }
        }
        composable(Screens.Appointment.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val time = navController.previousBackStackEntry?.savedStateHandle?.get<List<TimeSlot>>("time")
            val date = navController.previousBackStackEntry?.savedStateHandle?.get<LocalDate>("date")
            val barber = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")
            val services = navController.previousBackStackEntry?.savedStateHandle?.get<List<Service>>("services")
            val genders = navController.previousBackStackEntry?.savedStateHandle?.get<List<Int>>("genders")
            if (time != null && date != null && barber != null && services != null && genders != null) {
                DoubleCard(
                    midCarBody = {
                        SalonCard(
                            shopName = barber.shopName!!,
                            imageUri = barber.imageUri!!,
                            address = barber.shopStreetAddress!!,
                            distance = barber.distance!!,
                        )
                    },
                    navController,
                    topAppBar = {
                        BackButtonTopAppBar(
                            onBackClick = { navController.popBackStack() },
                            title = "Your Appointment"
                        )
                    },
                    bottomAppBar = {},
                    mainScreen = {
                        DetailScreen(time, date, barber, services, genders, navController)
                    })
            }
        }
    }
}
