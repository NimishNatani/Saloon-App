package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.viewmodel.MessageEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.data.model.ChatModel
import com.practicecoding.sallonapp.data.model.LastMessage
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MessageItemBox(
    navHostController: NavController,
    user: ChatModel,
    messageViewModel: MessageViewModel
) {
    val context = LocalContext.current
    val currentTime = LocalDateTime.now()
    val messageTime =
        LocalDateTime.parse(user.message.time, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    val duration = Duration.between(messageTime, currentTime)
    val timePassed = when {
        duration.toDays() > 0 -> "${duration.toDays()} days ago"
        duration.toHours() > 0 -> "${duration.toHours()} hours ago"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
        else -> "Just now"
    }
    Box(modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 4.dp)
        .border(
            2.dp, if (user.message.seenbyuser) Color.Black else sallonColor,
            RoundedCornerShape(10.dp)
        )
        .clickable {
            user.message.seenbyuser = true
            CoroutineScope(Dispatchers.IO).launch {
                messageViewModel.onEvent(MessageEvent.AddChat(user.message, user.uid, false))
            }
            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                key="user",
                value = user
            )
            navHostController.navigate(Screens.ChatScreen.route)
        })
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = user.image),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = user.message.message, color = Color.Gray, fontSize = 14.sp)
            }
            Text(
                text = timePassed,
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 2.dp)
            )
        }
    }
}