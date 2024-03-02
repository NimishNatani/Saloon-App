package com.practicecoding.sallonapp

sealed class Screens(val route: String) {
    object OnBoardingScreens : Screens("onboarding_screens")
    object OTPVerification : Screens("OTP_verification")
    object PhoneNumberScreen : Screens("phone_number_screen")
    object Logo : Screens("logo_screen")
}