package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.util.Log
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.OrderCard
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.ui.theme.purple_200
import kotlinx.coroutines.launch

@Composable
fun BookingHistoryScreen(
    completedOrderList: MutableList<OrderModel>,
    orderViewModel: OrderViewModel = hiltViewModel(),
    navController: NavController
) {
    BackHandler {
        navController.popBackStack()
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        containerColor = purple_200,
        topBar = {
            BackButtonTopAppBar(onBackClick = { navController.popBackStack() }, title ="Booking History" )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White)
                ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(completedOrderList) { order ->
                Spacer(modifier = Modifier.height(5.dp))
                OrderCard(

                    onCancel = {
                        order.orderStatus = OrderStatus.CANCELLED
                        scope.launch {
                            orderViewModel.updateOrderStatus(
                                order,
                                OrderStatus.CANCELLED.status
                            )
                        }
                    },
                    onContactBarber = { },
                    onReview = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("order", order)
                        navController.navigate(Screens.AddReviewScreen.route)
                        Log.d("OrderList", "OrderList: $order")
                    },
                    order = order
                )
//                HorizontalDivider(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    color = Color.Black,
//                    thickness = 1.dp,
//
//
//                )
            }
        }
    }
}