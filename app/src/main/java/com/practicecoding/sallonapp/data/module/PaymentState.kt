package com.practicecoding.sallonapp.data.module

sealed class PaymentState {
    data object Idle: PaymentState()
    data class Loading(val isLoading: Boolean): PaymentState()
    data class Success(val message: String,val paymentId: String): PaymentState()
    data class Error(val message: String): PaymentState()

}