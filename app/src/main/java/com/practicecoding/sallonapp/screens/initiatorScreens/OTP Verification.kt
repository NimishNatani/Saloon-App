package com.practicecoding.sallonapp.screens.initiatorScreens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhoneNumberScreen(
    //viewModel: LoginViewModel
    navigateToVerification: (String) -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Enter Phone Number",
            modifier = Modifier.padding(16.dp),
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif
        )
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
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                isError = phoneNumber.isBlank(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
            )
        }
        Button(
            onClick = {
                if (phoneNumber.isNotBlank()) {
                    navigateToVerification(phoneNumber)
                } else {
                    Toast.makeText(context,"Please provide your phone number.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 16.dp, horizontal = 16.dp)
                ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Next")
        }
    }
}


@Composable
fun OTPVerificationScreen(
    //viewModel: LoginViewModel
    phoneNumber: String) {
    var otp by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter OTP sent to $phoneNumber",
            modifier = Modifier.padding(16.dp),
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif
        )
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
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),

                isError = otp.isBlank(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )
        }
        Button(
            onClick = {
                /*TODO: Verify OTP and login system*/
                      //if user already exists then navigate to home screen
                        //else navigate to sign up screen
            },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            ),
        ) {
            Text("Verify")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PhoneNumberScreenPreview() {
    OTPVerificationScreen(phoneNumber = "1234567890")
    //PhoneNumberScreen(navigateToVerification = {})
}
