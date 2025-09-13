package com.practicecoding.sallonapp.appui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.practicecoding.sallonapp.data.config.RazorpayConfig
import com.practicecoding.sallonapp.data.module.PaymentState
import com.razorpay.Checkout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class PaymentViewModel: ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()

    fun startPayment(activity: Activity,amount:Int,name:String,phoneNumber: String,description:String = "Pay to ",){
        try {
            val options = JSONObject().apply {
                put("name","Saloon App")
                put("description","Pay to $name")
                put("currency","INR")
                put("amount",(amount*100).toLong())
                put("theme","#3399cc")
                put("method", JSONObject().apply {
                    put("upi",true)
                })
                put("upi", JSONObject().apply {
                    put("flow","intent")
                })
                put("readonly", JSONObject().apply {
                    put("contact",phoneNumber)
                    put("email",false)
                    put("method",false)
                })


            }
            val checkout = Checkout()
            checkout.setKeyID(RazorpayConfig.KEY_ID)
            checkout.open(activity,options)
        }catch (e:Exception){
            _paymentState.value = PaymentState.Error(e.message.toString())

        }

    }
    fun handlePaymentSuccess(paymentId: String){
        _paymentState.value = PaymentState.Success("Payment Successful",paymentId)
    }
    fun handlePaymentError(code:Int,errorMessage: String){
        _paymentState.value = PaymentState.Error(errorMessage)

    }
}