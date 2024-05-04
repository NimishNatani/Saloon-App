package com.practicecoding.sallonapp.appui

sealed class Screens(val route: String) {
    object OnBoardingScreens : Screens("onboarding_screens")
    object OTPVerification : Screens("OTP_verification")
    object PhoneNumberScreen : Screens("phone_number_screen")
    object Logo : Screens("logo_screen")
    object SignUp : Screens("sign_up")

    object MainScreen:Screens("main_screen")
    object BarberScreen:Screens("barber_screen")
    object GenderSelection:Screens("gender_screen")
    object ViewAllScreen:Screens("viewAll_screen")

}