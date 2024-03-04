package com.practicecoding.sallonapp.domain

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.practicecoding.sallonapp.data.AuthRepository
import com.practicecoding.sallonapp.data.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb: FirebaseAuth
) : AuthRepository {

    private lateinit var omVerificationCode:String




    override fun createUserWithPhone(phone: String,activity: Activity): Flow<Resource<String>> =  callbackFlow{
        trySend(Resource.Loading)

        val onVerificationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(Resource.Failure(p0))
            }

            override fun onCodeSent(verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, p1)
                trySend(Resource.Success("OTP Sent Successfully"))
                omVerificationCode = verificationCode
            }

        }

        val options = PhoneAuthOptions.newBuilder(authdb)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(onVerificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose{
            close()
        }
    }

    override fun signWithCredential(otp: String): Flow<Resource<String>>  = callbackFlow{
        trySend(Resource.Loading)
        val credential = PhoneAuthProvider.getCredential(omVerificationCode,otp)
        authdb.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("otp verified"))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}