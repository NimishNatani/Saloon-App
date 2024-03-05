package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.ui.theme.Purple40
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AdvancedSignUpScreen() {
    // State variables
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var birthDate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(Uri.parse("android.resource://${context.packageName}/${R.drawable.salon_app_logo}"))
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable( // Dismiss keyboard
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                // Background image
                AsyncImage(
                    model = selectedImageUri,

                    contentDescription = "Avatar Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1.0f)
                        .wrapContentSize()
                        .clip(shape = CircleShape)
                )
                // Icon for adding photo
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add Photo",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .align(Alignment.BottomEnd),
                    tint = Color.Black
                )
            }


            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(
                        sallonColor.toArgb()
                    ).copy(alpha = 0.6f),
                    unfocusedBorderColor = Color(
                        purple_200.toArgb()
                    ).copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    cursorColor = Color(
                        sallonColor.toArgb()
                    ), focusedLabelColor = Color(
                        sallonColor.toArgb()
                    ), unfocusedTextColor = Color.Black,

                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Name",
                        Modifier.size(16.dp),
                        tint = Color.Black

                    )
                },
                trailingIcon = {
                    if (name.isNotEmpty()) {
                        IconButton(onClick = { name = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = Color.Black)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )




            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    dropdownExpanded = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple_200),
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.toilet),
                        contentDescription = "drop down",
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = selectedGender?.label ?:"Male",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Start
                    )
                    androidx.compose.material3.DropdownMenu(
                        expanded = dropdownExpanded, onDismissRequest = {
                            dropdownExpanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Male", style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                selectedGender = Gender.MALE
                                dropdownExpanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Female",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                selectedGender = Gender.FEMALE
                                dropdownExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Other",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                selectedGender = Gender.OTHER
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
            // out line text field for birth date
            OutlinedTextField(
                value = birthDate.toString(),
                onValueChange = { birthDate = it },
                label = { Text("Birth Date") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(
                        sallonColor.toArgb()
                    ).copy(alpha = 0.6f),
                    unfocusedBorderColor = Color(
                        purple_200.toArgb()
                    ).copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    cursorColor = Color(
                        sallonColor.toArgb()
                    ), focusedLabelColor = Color(
                        sallonColor.toArgb()
                    ), unfocusedTextColor = Color.Black,

                    ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.birthdaycake),
                        contentDescription = "Name",
                        Modifier.size(16.dp),
                        tint = Color.Black

                    )
                },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
            )

            PasswordFields()

            Button(
                onClick = {

                    if (name.isNotBlank() && selectedGender != null && birthDate.isNotBlank()
                        && password.isNotBlank() && confirmPassword.isNotBlank() && (password == confirmPassword)
                    ) {
                        /*TODO: Sign up and saving data of user*/
                    } else {
                        Toast.makeText(
                            context,
                            "Either a field is empty or password and confirm password dont match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.sallon_color))
            ) {
                Text("Sign Up", color = Color.White) // Purple
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            Toast
                                .makeText(context, "Google", Toast.LENGTH_SHORT)
                                .show()
                        }
                )
                Spacer(modifier = Modifier.size(50.dp))
                Image(
                    painter = painterResource(id = R.drawable.icon_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            Toast
                                .makeText(context, "Facebook", Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }
        }
    }
}


@Composable
fun PasswordFields() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
    }
}


enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
}

//@Preview(showBackground = true)
//@Composable
//fun AdvancedSignUpScreenPreview() {
//          AdvancedSignUpScreen()
//
//}
