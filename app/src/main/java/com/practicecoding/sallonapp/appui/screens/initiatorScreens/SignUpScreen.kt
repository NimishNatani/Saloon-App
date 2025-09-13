package com.practicecoding.sallonapp.appui.screens.initiatorScreens

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.utils.showMsg
import com.practicecoding.sallonapp.appui.viewmodel.AddUserDataViewModel
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.UserModel
import com.practicecoding.sallonapp.ui.theme.Purple80
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AdvancedSignUpScreen(
    phoneNumber: String = "",
    activity: Activity,
    viewModel: AddUserDataViewModel = hiltViewModel(),
    navController: NavController,

    ) {
    var phone by remember { mutableStateOf("") }

    if (phoneNumber!="000") {
        phone=phoneNumber
    }
    // State variables
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    var textFieldPosition by remember { mutableStateOf(Offset.Zero) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    var selectedGender by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(Uri.parse("android.resource://${context.packageName}/${R.drawable.salon_app_logo}"))
    }
    var isDialogVisible by remember { mutableStateOf(false) }

    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.parse("android.resource://${context.packageName}/${R.drawable.salon_app_logo}"))
    }
    val scope = rememberCoroutineScope()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            selectedImageUri = imageUri
        }
    val gender = listOf("Male", "Female", "Other")

    if (isDialogVisible) {
        CommonDialog()
    }


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
                    model = imageUri,

                    contentDescription = "Avatar Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1.0f)
                        .width(150.dp)
                        .height(150.dp)
                        .clip(shape = CircleShape)
                )
                // Icon for adding photo
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add Photo",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                        .align(Alignment.BottomEnd),
                    tint = Color.Black
                )
            }
            OutlinedTextField(
                value = phone.toString(),
                enabled = true,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80, // Change the outline color when focused
                    unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_phone_50),
                        contentDescription = "Name",
                        Modifier.size(16.dp),
                        tint = Color.Black

                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80, // Change the outline color when focused
                    unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                    errorBorderColor = purple_200
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
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black,

                                )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )




            OutlinedTextField(
                value = selectedGender,
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.SemiBold),
                onValueChange = { selectedGender = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                        textFieldPosition = coordinates.positionInWindow()
                    }
                    .padding(horizontal = 1.dp),
                label = {
                    Text(
                        "Select a gender",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                trailingIcon = {
                    Icon(
                        icon,
                        contentDescription = null,
                        Modifier.clickable { mExpanded = !mExpanded },
                        tint = Color.Black
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = Color(0xFFBB86FC),
                    unfocusedBorderColor = Color(0xFF6200EE),
                    disabledTrailingIconColor = Color.White
                ),
                enabled = false
            )

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                    .background(if (mExpanded) Color.White else Color.Black),
                offset = DpOffset(
                    x = 0.dp,
                    y = with(LocalDensity.current) { textFieldPosition.y.toDp() + mTextFieldSize.height.toDp() }
                )
            ) {
                gender.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label, color = Color.Black) },
                        onClick = {
                            selectedGender = label
                            mExpanded = false
                        }
                    )
                }
            }
            // out line text field for birth date

            GeneralButton(
                text = "Sign In",
                width = 350,
                height = 80,
                modifier = Modifier,
                roundnessPercent = 50
            ) {
                if (name.isNotBlank() && selectedGender != null && phone?.length == 10
                ) {
                    val userModel = UserModel(
                        name,
                        phone,
                        selectedGender.toString(),
                        selectedImageUri.toString()
                    )
                    scope.launch(Dispatchers.Main) {
                        viewModel.addUserData(userModel, selectedImageUri, activity).collect {
                            when (it) {
                                is Resource.Success -> {
                                    isDialogVisible = false
                                    activity.showMsg(it.result)
                                    navController.navigate(Screens.MainScreen.route)
                                }

                                is Resource.Failure -> {
                                    isDialogVisible = false
                                    activity.showMsg(it.exception.toString())
                                }

                                Resource.Loading -> {
                                    isDialogVisible = true
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(
                        context,
                        "A field is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
//    val context = LocalContext.current
//AdvancedSignUpScreen(phoneNumber = "1234567890",activity = context as Activity)
//}
