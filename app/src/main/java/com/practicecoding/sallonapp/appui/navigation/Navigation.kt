package com.practicecoding.sallonapp.appui.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.HeadingText
import com.practicecoding.sallonapp.appui.components.SalonCard
import com.practicecoding.sallonapp.appui.screens.MainScreens.AddReviewScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.BarberScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.BarberServiceVise
import com.practicecoding.sallonapp.appui.screens.MainScreens.BookingHistoryScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.BottomSheet
import com.practicecoding.sallonapp.appui.screens.MainScreens.CategoriesScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.ChatScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.DetailScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.FavBarberListScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.GenderSelectOnBook
import com.practicecoding.sallonapp.appui.screens.MainScreens.MainScreen1
import com.practicecoding.sallonapp.appui.screens.MainScreens.PaymentScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.ServiceSelector
import com.practicecoding.sallonapp.appui.screens.MainScreens.SortBarber
import com.practicecoding.sallonapp.appui.screens.MainScreens.TimeSelection
import com.practicecoding.sallonapp.appui.screens.MainScreens.UpdateUserInfoScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.ViewAllScreen
import com.practicecoding.sallonapp.appui.screens.MainScreens.daySelection
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.AdvancedSignUpScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OTPVerificationScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.PhoneNumberScreen
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.BookingModel
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.Service
import com.practicecoding.sallonapp.data.model.ServiceCategoryModel
import com.practicecoding.sallonapp.data.model.TimeSlot
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import java.time.LocalDate

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestinations: String,
) {
    val enterTransition =
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )

    val exitTransition =
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val popEnterTransition =
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )

    val popExitTransition =
        slideOutHorizontally(
            targetOffsetX = { it },
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
        composable(Screens.MainScreen.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            MainScreen1(navController, context)

        }
        composable(Screens.ViewAllScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val resultType =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("type")
            val resultCity =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("location")
            if (resultCity != null && resultType != null) {
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

        composable(Screens.BarberScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val bookingModel =
                navController.previousBackStackEntry?.savedStateHandle?.get<BookingModel>("bookingModel")
            if (bookingModel != null) {
                BarberScreen(
                    onBackClick = { navController.popBackStack() },
                    onLikeClick = { /* Handle like click */ },
                    onShareClick = { /* Handle share click */ },
                    bookingModel = bookingModel,
                    navController = navController
                )
            }
        }
        composable(Screens.GenderSelection.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val bookingModel =
                navController.previousBackStackEntry?.savedStateHandle?.get<BookingModel>("bookingModel")
            if (bookingModel != null) {
                GenderSelectOnBook(
                    navController = navController,
                    bookingModel = bookingModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(Screens.serviceSelector.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val bookingModel =
                navController.previousBackStackEntry?.savedStateHandle?.get<BookingModel>("bookingModel")
            if (bookingModel != null) {
                ServiceSelector(
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    bookingModel = bookingModel
                )}
        }
        composable(Screens.DayTimeSelection.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val currentDate = LocalDate.now()
            val bookingModel =
                navController.previousBackStackEntry?.savedStateHandle?.get<BookingModel>("bookingModel")
            if (bookingModel != null) {
                val time = bookingModel.listOfService.sumOf { it.time.toInt() * it.count }
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
                        TimeSelection(
                            time,
                            date,
                            navController,
                            bookingModel
                        )
                    })
            }
        }
        composable(Screens.Appointment.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val bookingModel =
                navController.previousBackStackEntry?.savedStateHandle?.get<BookingModel>("bookingModel")
            if (bookingModel != null) {
                DoubleCard(
                    midCarBody = {
                        SalonCard(
                            shopName = bookingModel.barber.shopName!!,
                            imageUri = bookingModel.barber.imageUri!!,
                            address = bookingModel.barber.shopStreetAddress!!,
                            distance = bookingModel.barber.distance!!,
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
                        DetailScreen(bookingModel, navController)
                    })
            }
        }
        composable(Screens.UpiPayment.route) {
            PaymentScreen()
        }
        composable(Screens.ChatScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val user =
                navController.previousBackStackEntry?.savedStateHandle?.get<ChatModel>("user")
            if (user != null) {
                ChatScreen(user, navController)
            }
        }
        composable(Screens.AddReviewScreen.route) {
            val orderModel = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<OrderModel>("order")
            Log.d("Navigation", "AppNavigation: $orderModel")
            if (orderModel != null) {
                AddReviewScreen(order = orderModel, navController = navController)
            }
        }
        composable(Screens.UpdateProfileScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            UpdateUserInfoScreen(navController = navController)
        }
        composable(Screens.BookingHistory.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            BookingHistoryScreen(navController = navController)
        }
        composable(Screens.FavBarberList.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            FavBarberListScreen(
                likedBarberViewModel = LikedBarberViewModel(context),
                navController = navController
            )
        }
        composable(Screens.CatBarberList.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val service = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("service") ?: "default_service"
            BarberServiceVise(
                service = service,
                likedBarberViewModel = LikedBarberViewModel(context),
                navController = navController
            )
        }
        composable(Screens.AllCategory.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            CategoriesScreen(navController = navController)
        }
    }
}
