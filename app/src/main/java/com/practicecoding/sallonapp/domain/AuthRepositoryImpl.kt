package com.practicecoding.sallonapp.domain

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.practicecoding.sallonapp.data.AuthRepository
import com.practicecoding.sallonapp.data.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb: FirebaseAuth,
    @ApplicationContext private val context: Context

) : AuthRepository {
    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


    override fun createUserWithPhone(phone: String, activity: Activity): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)

            val onVerificationCallback =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(Resource.Failure(p0))
                    }

                    override fun onCodeSent(
                        verificationCode: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationCode, p1)
                        trySend(Resource.Success("OTP Sent Successfully"))
                        sharedPreferences.edit().putString("verification_code", verificationCode)
                            .apply()
                    }

                }

            val options = PhoneAuthOptions.newBuilder(authdb)
                .setPhoneNumber("+91$phone")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallback)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            awaitClose {
                close()
            }
        }

    override fun signWithCredential(otp: String): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        val savedVerificationCode = sharedPreferences.getString("verification_code", null)
        if (savedVerificationCode != null) {
            val credential = PhoneAuthProvider.getCredential(savedVerificationCode, otp)
            authdb.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        trySend(Resource.Success("OTP Verified"))
                    else
                        trySend(Resource.Failure(Exception("Failed to sign in with credentials")))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
        } else {
            trySend(Resource.Failure(Exception("Verification code not found")))
        }
        awaitClose {
            close()
        }
    }
}