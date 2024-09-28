package com.practicecoding.sallonapp.appui.screens.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.MessageItemBox
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.practicecoding.sallonapp.ui.theme.sallonColor

@Composable
fun MessageScreen(navHostController: NavController,viewModel: MessageViewModel ,    viewModelBarber: GetBarberDataViewModel
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    BackHandler {
        viewModelBarber.navigationItem.value = NavigationItem.Home
    }
    DoubleCard(
        midCarBody = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = sallonColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                        )
                        .background(if (selectedTabIndex == 0) purple_200 else Color.White)

                ) {
                    Text(
                        "Chat",
                        color = if (selectedTabIndex == 0) Color.Black else Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                        .background(if (selectedTabIndex == 1) purple_200 else Color.White)

                ) {
                    Text(
                        "Call",
                        color = if (selectedTabIndex == 1) Color.Black else Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        },
        mainScreen = {
            if(selectedTabIndex == 0) {
                MessageList(navHostController, viewModel)
            }
        },
        topAppBar = {
            Text(
                text = "Message",
                modifier = Modifier.fillMaxWidth().padding(top=15.dp, bottom = 20.dp)
                ,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
//            BackButtonTopAppBar(onBackClick = { /*TODO*/ }, title = "Message")
        },
    )
}

@Composable
fun MessageList(navHostController: NavController,viewModel: MessageViewModel ){
    var refresh by remember {
        mutableStateOf(true)
    }
    val user by remember {
        mutableStateOf(viewModel.userChat.value)
    }

//    LaunchedEffect(refresh) {
//viewModel.onEvent(MessageEvent.GetChatBarber)
//    }
    if (viewModel.userChat.value.isNotEmpty()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            viewModel.userChat.value.reversed().forEach { chatModel ->
                MessageItemBox(
                    navHostController = navHostController,
                    message = chatModel.message,
                    image = chatModel.image,
                    name = chatModel.name,
                    uid = chatModel.uid,
                    phoneNumber = chatModel.phoneNumber,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun TabsUi(){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = sallonColor,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            modifier = Modifier
                .clip(
                    RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .background(if (selectedTabIndex == 0) purple_200 else Color.White)

        ) {
            Text(
                "Chat",
                color = if (selectedTabIndex == 0) Color.Black else Color.Gray,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { selectedTabIndex = 1 },
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selectedTabIndex == 1) purple_200 else Color.White)

        ) {
            Text(
                "Call",
                color = if (selectedTabIndex == 1) Color.Black else Color.Gray,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}