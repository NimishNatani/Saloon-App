package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.screens.DoubleCard
import com.practicecoding.sallonapp.ui.theme.Purple80
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
//                isError = phoneNumber.isBlank(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80, // Change the outline color when focused
                    unfocusedBorderColor = Purple80, // Change the outline color when unfocused
                    errorBorderColor = Purple80
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
//                .background(color = sallonColor)
                ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = sallonColor,
                contentColor = Color.White
            )
        ) {
            Text(text="Next")
        }
    }
}


@Composable
fun OTPVerificationScreen(
    //viewModel: LoginViewModel
    phoneNumber: String) {
    var otpText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
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
        Row(
              modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "We sent the OTP",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 16.sp
            )
            ClickableTextWithUnderline(
            text = "Didn't get it? Resend",
            onClick = {
                //viewModel.resendOtp(phoneNumber)
                Toast.makeText(context,"Resend OTP", Toast.LENGTH_SHORT).show()
            }
        )
        }

    }

}
@Composable
fun ClickableTextWithUnderline(text: String, onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = contentColorFor(MaterialTheme.colorScheme.background),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(text)
        }
        addStringAnnotation(
            tag = "Clickable",
            start = text.indexOf("Didn't get it?"),
            end = text.indexOf("Didn't get it?") + "Didn't get it?".length,
            annotation = "Clickable"
        )
        withStyle(
            style = SpanStyle(
                color = colorResource(id = R.color.blue_grad1), // Set to the desired color (blue in this case)
                textDecoration = TextDecoration.None
            )
        ) {
            addStringAnnotation(
                tag = "Clickable",
                start = text.indexOf("Didn't get it?"),
                end = text.indexOf("Didn't get it?") + "Didn't get it?".length,
                annotation = "Clickable"
            )
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "Clickable",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClick()
            }
        },
        modifier = Modifier.padding(end = 16.dp)
    )
}



@Preview(showBackground = true)
@Composable
fun PhoneNumberScreenPreview() {
    DoubleCard(title = "Verfication",  {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Signup", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }) {
        OTPVerificationScreen(phoneNumber = "1234567890")
    }

}
