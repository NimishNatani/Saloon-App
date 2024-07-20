package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.appui.viewmodel.MessageEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.data.model.Message
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    image: String,
    name: String,
    uid: String,
    phoneNumber: String,
    navController: NavController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    BackHandler {
        navController.currentBackStackEntry?.savedStateHandle?.set("navigationTo", NavigationItem.Message)
        navController.navigate(Screens.MainScreen.route){
            popUpTo(Screens.ChatScreen.route) {
                inclusive = true
            }        }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(MessageEvent.MessageList(uid))

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(purple_200)
        ) {
            TopBar(image, name, phoneNumber)
            ChatMessages(modifier = Modifier.weight(1f), viewModel)

        }
        MessageInput(uid)
    }
}

@Composable
fun TopBar(image: String, name: String,phoneNumber: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(purple_200)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(model = image),
            contentDescription = "User Image",
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, color = Color.Black, fontSize = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call",
            tint = sallonColor,
            modifier = Modifier.clickable {
                val u = Uri.parse("tel:$phoneNumber")

                // Create the intent and set the data for the
                // intent as the phone number.
                val i = Intent(Intent.ACTION_DIAL, u)
                try {

                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    context.startActivity(i)
                } catch (s: SecurityException) {

                    // show() method display the toast with
                    // exception message.
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }
        )
    }
}

@Composable
fun ChatMessages(modifier: Modifier = Modifier, viewModel: MessageViewModel) {
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.messageList) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    if (viewModel.messageList.isNotEmpty()) {
        Column(
            modifier = modifier
                .padding(top = 18.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var lastDate = ""
            viewModel.messageList.forEach { message ->
                val currentDate = parseDate(message.time)
                if (currentDate != lastDate) {
                    DateSeparator(date = currentDate)
                    lastDate = currentDate
                }
                ChatBubble(
                    message = message.message,
                    time = formatTime(message.time),
                    isSent = message.status
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DateSeparator(date: String) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = date, color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun ChatBubble(message: String, time: String, isSent: Boolean) {
    Row(
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isSent) {
                    54.dp
                } else 16.dp,
                end = if (isSent) {
                    16.dp
                } else {
                    54.dp
                }
            )
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isSent) sallonColor else purple_200,
                    shape = if (isSent) RoundedCornerShape(
                        16.dp,
                        0.dp,
                        16.dp,
                        16.dp
                    ) else RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message,
                color = if (isSent) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun MessageInput(uid: String, viewModel: MessageViewModel = hiltViewModel()) {
    var textState by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(

            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .weight(1f)
                .heightIn(max = 120.dp),
            placeholder = { Text(text = "Type a message") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = sallonColor,
                unfocusedIndicatorColor = sallonColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = sallonColor,
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,

                ),
        )
        IconButton(onClick = {
            if (textState.isNotEmpty()) {
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)
                val message = Message(true, textState, formattedDate)
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.onEvent(MessageEvent.AddChat(message,uid))

                }
                textState=""
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = sallonColor,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


fun parseDate(time: String): String {
    val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val date = inputFormat.parse(time)
    return date?.let { outputFormat.format(it) } ?: ""
}

fun formatTime(time: String): String {
    val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(time)
    return date?.let { outputFormat.format(it) } ?: ""
}

