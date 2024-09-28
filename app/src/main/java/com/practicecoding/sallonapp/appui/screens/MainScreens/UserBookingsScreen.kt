package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.appui.components.OrderCard
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.launch

@Composable
fun UserOrderPage(
    navController: NavController,
    context: Context,
    orderViewModel: OrderViewModel ,
    viewModelBarber:GetBarberDataViewModel
){
    if(orderViewModel.isUpdating.value){
        CommonDialog(title = "Updating...")
    }
    BackHandler {
        viewModelBarber.navigationItem.value = NavigationItem.Home
    }
    DoubleCard(
        midCardAble = false,
        midCarBody = {
          //UpcomingOrderCard(upcomingOrder = orderViewModel.upcomingOrder)
        },
        mainScreen = {
            UserBookingScreen(
                activity = context as Activity,
                orderViewModel= orderViewModel,
                navController = navController
            )
        },
        topAppBar = {
            Text(
                text = "Booking Status",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 20.dp)
                ,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    )
}

@Composable
fun OrderList(orders: List<OrderModel>,
              orderViewModel: OrderViewModel = hiltViewModel(),
              navController: NavController,
) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderCard(
                imageUrl = order.imageUrl,
                orderType = order.orderType,
                timeSlot = order.timeSlot,
                phoneNumber = order.phoneNumber,
                onCancel = {
                  order.orderStatus = OrderStatus.CANCELLED
                  scope.launch{
                      orderViewModel.updateOrderStatus(order.orderId, OrderStatus.CANCELLED.status)
                  }
                },
                onContactBarber = { },
                onReview = {
                navController.currentBackStackEntry?.savedStateHandle?.set("order", order)
                navController.navigate(Screens.AddReviewScreen.route)
                    Log.d("OrderList", "OrderList: $order")
                           },
                barberName = order.barberName,
                barbershopName = order.barberShopName,
                orderStatus = order.orderStatus,
                date = order.date,
                orderID = order.orderId,
                paymentMethod = order.paymentMethod!!,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserBookingScreen(
    activity: Activity,
    orderViewModel: OrderViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, pageCount = {4})
    val selectedTab = remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedTab.value,
                containerColor = Color(sallonColor.toArgb()),
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                Tab(
                    text = { Text("Pending", color = Color.White) },
                    selected = selectedTab.value == 0,
                    onClick = {
                        selectedTab.value = 0
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    },
                    modifier = Modifier.clip(CircleShape)
                )
                Tab(
                    text = { Text("Accepted", color = Color.White) },
                    selected = selectedTab.value == 1,
                    onClick = {
                        selectedTab.value = 1
                        scope.launch {
                            pagerState.scrollToPage(1)
                        }
                    }
                )
                Tab(
                    text = { Text("Completed", color = Color.White) },
                    selected = selectedTab.value == 2,
                    onClick = {
                        selectedTab.value = 2
                        scope.launch {
                            pagerState.scrollToPage(2)
                        }
                    }
                )
                Tab(
                    text = { Text("Cancelled", color = Color.White) },
                    selected = selectedTab.value == 3,
                    onClick = {
                        selectedTab.value = 3
                        scope.launch {
                            pagerState.scrollToPage(3)
                        }
                    }
                )
            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .height(screenHeight.dp)
                    .background(Color.White)
                    .border(
                        border = BorderStroke(2.dp, Color(sallonColor.toArgb()))
                    )
            ) { page ->
                when (page) {
                    0 -> OrderList(orders = orderViewModel.pendingOrderList.value.toMutableList(), navController = navController,)
                    1 -> OrderList(orders = orderViewModel.acceptedOrderList.value.toMutableList(), navController = navController,)
                    2 -> OrderList(orders = orderViewModel.completedOrderList.value.toMutableList(), navController = navController,)
                    3 -> OrderList(orders = orderViewModel.cancelledOrderList.value.toMutableList(), navController =navController,)
                }
            }
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        if (selectedTab.value != pagerState.currentPage) {
            selectedTab.value = pagerState.currentPage
        }
    }
}