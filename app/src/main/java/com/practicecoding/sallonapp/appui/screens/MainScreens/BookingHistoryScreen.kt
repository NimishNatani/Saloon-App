package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practicecoding.sallonapp.R
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.components.BigSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.BottomAppNavigationBar
import com.practicecoding.sallonapp.appui.components.Categories
import com.practicecoding.sallonapp.appui.components.DoubleCard
import com.practicecoding.sallonapp.appui.components.NavigationItem
import com.practicecoding.sallonapp.appui.components.OfferCard
import com.practicecoding.sallonapp.appui.components.OrderCard
import com.practicecoding.sallonapp.appui.components.ProfileWithNotification
import com.practicecoding.sallonapp.appui.components.SearchBar
import com.practicecoding.sallonapp.appui.components.ShimmerEffectBarber
import com.practicecoding.sallonapp.appui.components.ShimmerEffectMainScreen
import com.practicecoding.sallonapp.appui.components.SmallSaloonPreviewCard
import com.practicecoding.sallonapp.appui.components.TransparentTopAppBar
import com.practicecoding.sallonapp.appui.viewmodel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.viewmodel.LocationViewModel
import com.practicecoding.sallonapp.appui.viewmodel.MainEvent
import com.practicecoding.sallonapp.appui.viewmodel.MessageViewModel
import com.practicecoding.sallonapp.appui.viewmodel.OrderViewModel
import com.practicecoding.sallonapp.data.model.BarberModel
import com.practicecoding.sallonapp.data.model.LocationModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.locationObject
import com.practicecoding.sallonapp.room.LikedBarberViewModel
import com.practicecoding.sallonapp.ui.theme.sallonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun BookingHistoryScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    navController: NavController
){
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onBackClick = {
                        navController.navigate(route = Screens.MainScreen.route,){
                            popUpTo(Screens.BookingHistory.route){
                                inclusive = false
                            }
                        }
                              },
                onLikeClick = { /*TODO*/ },
                onShareClick = { /*TODO*/ },
                isFavorite = false,
                usingInProfile = true
            )
        },
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it).background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(orderViewModel.orderList.value) { order ->
                OrderCard(
                    imageUrl = order.imageUrl,
                    orderType = order.orderType,
                    timeSlot = order.timeSlot,
                    phoneNumber = order.phoneNumber,
                    onCancel = {
                        order.orderStatus = OrderStatus.CANCELLED
                        scope.launch {
                            orderViewModel.updateOrderStatus(
                                order.orderId,
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
                    barberName = order.barberName,
                    barbershopName = order.barberShopName,
                    orderStatus = order.orderStatus,
                    date = order.date,
                    orderID = order.orderId,
                    paymentMethod = order.paymentMethod!!,
                )
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    startIndent = 10.dp
                )
            }
        }
    }
}