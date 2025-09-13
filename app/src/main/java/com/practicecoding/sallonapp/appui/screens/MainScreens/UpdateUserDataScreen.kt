package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarber
import com.practicecoding.sallonapp.appui.viewmodel.GetUserDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.UserEvent
import com.practicecoding.sallonapp.appui.viewmodel.AllUserDataViewModel
import com.practicecoding.sallonapp.data.model.UserModel
import com.practicecoding.sallonapp.ui.theme.Purple80
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UpdateUserInfoScreen(
    navController: NavController,
    viewModel: GetUserDataViewModel = hiltViewModel(),
    allUserDataViewModel: AllUserDataViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val user by allUserDataViewModel.user.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val phoneNumber = remember { mutableStateOf(TextFieldValue("")) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(user) {
        user.let {
            name.value = TextFieldValue(it.name ?: "")

            phoneNumber.value = TextFieldValue(it.phoneNumber ?: "")
            imageUri = it.imageUri?.toUri()
        }
    }

    if (viewModel.isLoading.value) {
        LoadingAnimation()
    }
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                selectedImageUri = it
                imageUri = it
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(vertical = 30.dp, horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(text = "Update Your Info", color = Color.Black, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
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
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add Photo",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                        .align(Alignment.BottomEnd),
                    tint = Color.Black
                )
            }

            listOf(
                Triple(name, "Name", R.drawable.img),
                Triple(phoneNumber,"Phone Number",Icons.Filled.Phone)
            ).forEach { (textFieldState, label, icon) ->
                Spacer(modifier = Modifier.height(8.dp))
                CustomOutlinedTextField(textFieldState, label, icon)
            }

            GeneralButton(
                text = "Update",
                width = 350,
                height = 80
            ) {
                val updatedUser = UserModel(
                    name = name.value.text.takeIf { it.isNotEmpty() } ?: user?.name,
                    phoneNumber = phoneNumber.value.text.takeIf { it.isNotEmpty() } ?: user?.phoneNumber,
                    imageUri = imageUri.toString(),
                )
                scope.launch(Dispatchers.IO) {
                    viewModel.onEvent(
                        UserEvent.UpdateUser(
                            updatedUser,
                            selectedImageUri ,
                            context,
                            navController
                        )
                    )
                }

        }
    }
}

@Composable
fun CustomOutlinedTextField(
    state: MutableState<TextFieldValue>,
    label: String,
    icon: Any
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple80,
            unfocusedBorderColor = purple_200,
            errorBorderColor = purple_200
        ),
        leadingIcon = {
            when (icon) {
                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    Modifier.size(16.dp),
                    tint = Color.Black
                )
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    Modifier.size(16.dp),
                    tint = Color.Black
                )
            }
        },
        trailingIcon = {
            if (state.value.text.isNotEmpty()) {
                IconButton(onClick = { state.value = TextFieldValue("") }) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Black
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}



@Preview(showBackground = true)
@Composable
fun UpdateBarberInfoScreenPreview() {
    val navController = rememberNavController()
    UpdateUserInfoScreen(navController)
}

