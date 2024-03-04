package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.utils.showMsg
import com.practicecoding.sallonapp.appui.viewmodel.AuthViewModel
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.ui.theme.Purple80
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PhoneNumberScreen(activity:Activity,
                      viewModel: AuthViewModel = hiltViewModel(),
                      navigateToVerification: (String) -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDialog by remember{ mutableStateOf(false)}

    val focusManager = LocalFocusManager.current

    if(isDialog)
        CommonDialog()

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentSize()
                .background(Color.White)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
        ) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number", color = Purple80) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 40.dp),
//                isError = phoneNumber.isBlank(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80, // Change the outline color when focused
                    unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                    errorBorderColor = purple_200
                ),
            )
        }
        GeneralButton(text = "SignUp", width = 250, height = 50, modifier = Modifier) {
            if (phoneNumber.isNotBlank()&&phoneNumber.length!=10) {
                scope.launch(Dispatchers.Main){
                    viewModel.createUserWithPhone(
                        phoneNumber,
                        activity
                    ).collect{
                        when(it){
                            is Resource.Success->{
                                isDialog = false
                                activity.showMsg(it.result)
                            }
                            is Resource.Failure->{
                                isDialog = false
                                activity.showMsg(it.exception.toString())
                            }
                            Resource.Loading->{
                                isDialog = true
                            }
                        }
                    }
                    navigateToVerification(phoneNumber)

                }
                } else {
                    Toast.makeText(context,"Please provide your phone number.", Toast.LENGTH_SHORT).show()
                }
        }

    }
}


@Composable
fun OTPVerificationScreen(
    //viewModel: LoginViewModel
    phoneNumber: String) {
    var otpText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) { index ->
                OutlinedTextField(
                    value = otpText.getOrNull(index)?.toString() ?: "",
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            otpText = otpText.substring(0 until index) + newValue + otpText.substring((index + 1).coerceAtMost(otpText.length))
                            if (newValue.isNotEmpty() && index < 5) {
                                focusManager.moveFocus(FocusDirection.Right)
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == 5) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (index < 5) {
                                focusManager.moveFocus(FocusDirection.Right)
                            }
                        }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PhoneNumberScreenPreview() {
//    OTPVerificationScreen(phoneNumber = "1234567890")
//    PhoneNumberScreen(navigateToVerification = {})
}
