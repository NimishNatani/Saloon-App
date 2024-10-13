package com.practicecoding.sallonapp.appui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.Identity
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.GoogleAuthUiClient
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.SignInResult
import com.practicecoding.sallonapp.appui.screens.initiatorScreens.SignInState
import com.practicecoding.sallonapp.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun signIn(context: Context, onResult: (IntentSender?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val googleAuthUiClient by lazy {
                GoogleAuthUiClient(
                    context = context,
                    oneTapClient = Identity.getSignInClient(context)
                )
            }
            onResult(googleAuthUiClient.signIn())
        }
    }

    fun signInWithIntent(context: Context,result: ActivityResult, onResult: (SignInResult) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val googleAuthUiClient by lazy {
                GoogleAuthUiClient(
                    context = context,
                    oneTapClient = Identity.getSignInClient(context)
                )
            }
            onResult(
                googleAuthUiClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
            )
        }
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun createUserWithPhone(
        mobile: String, activity: Activity
    ) = repo.createUserWithPhone(mobile, activity)

    fun signInWithCredential(
        code: String
    ) = repo.signWithCredential(code)

}