package com.practicecoding.sallonapp.appui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.data.FireStoreDbRepository
import com.practicecoding.sallonapp.data.Resource
import com.practicecoding.sallonapp.data.model.OrderModel
import com.practicecoding.sallonapp.data.model.OrderStatus
import com.practicecoding.sallonapp.data.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
    private val _orderList = mutableStateOf<List<OrderModel>>(emptyList())
    val orderList: State<List<OrderModel>> = _orderList
    private val _acceptedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val acceptedOrderList: State<List<OrderModel>> = _acceptedOrderList
    private val _pendingOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val pendingOrderList: State<List<OrderModel>> = _pendingOrderList
    private val _completedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val completedOrderList: State<List<OrderModel>> = _completedOrderList
    private val _pendingCancelOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val pendingCancelList: State<List<OrderModel>> = _pendingCancelOrderList
    var isLoading = mutableStateOf(false)
    var upcomingOrder = mutableStateOf(OrderModel(
        barberShopName = " ",
        barberName = " ",
        imageUrl = " ",
        orderType = emptyList(),
        phoneNumber = " ",
        timeSlot = emptyList(),
    ))

    init {
        viewModelScope.launch { getOrders() }
    }
    suspend fun onEvent(event: OrderEvent, orderId: String = " ", review: ReviewModel = ReviewModel(),onCompletion:()->Unit = {}) {
        when(event) {
            is OrderEvent.GetOrderList -> getOrders()
            is OrderEvent.AddReview -> addReview(orderId, review, onCompletion)
            is OrderEvent.UpdateOrderStatus -> {}
        }
    }

    private suspend fun addReview(orderId: String, review: ReviewModel,onCompletion: () -> Unit) {
        repo.addReview(orderId, review).collect {
            when(it) {
                is Resource.Success -> {
                    Log.d("rOrderViewModel", "addReview: Success")
                    onCompletion()
                }
                is Resource.Failure -> {
                    Log.d("rOrderViewModel", "addReview: Error ${it.exception}")
                }
                else -> {}
            }
        }
    }

    private suspend fun getOrders() {
        isLoading.value = true
        repo.getOrders { orders ->
            Log.d("OrderViewModel", "getOrders: $orders")
            Log.d("rOrderViewModel", "getOrders: ${orders.reversed()}")
            _orderList.value = orders.reversed()
            _acceptedOrderList.value = orders.filter { it.orderStatus == OrderStatus.ACCEPTED }
            _pendingOrderList.value = orders.filter { it.orderStatus == OrderStatus.PENDING }
            _completedOrderList.value = orders.filter { it.orderStatus == OrderStatus.COMPLETED || it.orderStatus == OrderStatus.CANCELLED }
            _pendingCancelOrderList.value = orders.filter { it.orderStatus == OrderStatus.PENDING_CANCELLATION }
            upcomingOrder.value = _acceptedOrderList.value.firstOrNull() ?: OrderModel(
                barberShopName = " ",
                barberName = " ",
                imageUrl = " ",
                orderType = emptyList(),
                phoneNumber = " ",
                timeSlot = emptyList(),
            )
            Log.d("OrderViewModel", "getOrders: ${upcomingOrder.value}")
            Log.d("aOrderViewModel", "getOrders: ${acceptedOrderList.value}")
            Log.d("pOrderViewModel", "getOrders: ${pendingOrderList.value}")
            isLoading.value = false
        }
    }
    suspend fun updateOrderStatus(orderId: String, status: String) {
        repo.updateOrderStatus(orderId, status).collect(){
            when(it){
                is Resource.Success -> {
                    Log.d("OrderViewModel", "updateOrderStatus: Success")
                    Log.d("OrderViewModel", "updateOrderStatus: ${LocalDate.now()}")
                }
                is Resource.Failure -> {
                    Log.d("OrderViewModel", "updateOrderStatus: Error")
                }
                else -> {}
            }
        }
    }
}
sealed class OrderEvent {
    object GetOrderList : OrderEvent()
    object UpdateOrderStatus : OrderEvent()
    object AddReview : OrderEvent()
}