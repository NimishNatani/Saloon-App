package com.practicecoding.sallonapp.appui.viewmodel

import android.util.Log
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
) : ViewModel() {
    private val _orderList = mutableStateOf<List<OrderModel>>(emptyList())
    val orderList: State<List<OrderModel>> = _orderList
    private val _acceptedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val acceptedOrderList: State<List<OrderModel>> = _acceptedOrderList
    private val _pendingOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val pendingOrderList: State<List<OrderModel>> = _pendingOrderList
    private val _completedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val completedOrderList: State<List<OrderModel>> = _completedOrderList
    private val _cancelledOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val cancelledOrderList: State<List<OrderModel>> = _cancelledOrderList
    var isLoading = mutableStateOf(false)

    private val _reviewList = mutableStateOf<List<ReviewModel>>(emptyList())
    val reviewList: State<List<ReviewModel>> = _reviewList

    var upcomingOrder = mutableStateOf(
        OrderModel(
            barberShopName = " ",
            barberName = " ",
            imageUrl = " ",
            orderType = emptyList(),
            phoneNumber = " ",
            timeSlot = emptyList(),
        )
    )

    init {
        viewModelScope.launch { getOrders() }
    }

    suspend fun onEvent(
        event: OrderEvent,
        orderId: String = " ",
        review: ReviewModel = ReviewModel(),
        onCompletion: () -> Unit = {}
    ) {
        when (event) {
            is OrderEvent.GetOrderList -> getOrders()
            is OrderEvent.AddReview -> addReview(orderId, review, onCompletion)
            is OrderEvent.UpdateOrderStatus -> {}
        }
    }

    private suspend fun addReview(orderId: String, review: ReviewModel, onCompletion: () -> Unit) {
        repo.addReview(orderId, review).collect {
            when (it) {
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

    fun getReviewByOrderId(orderId: String): ReviewModel? {
        val review = _reviewList.value.find { it.orderId == orderId }
        return if (review != null) {
            review
        } else {
            null
        }
    }

    private suspend fun getReviewList() {
        repo.getReview().collect {
            when (it) {
                is Resource.Success -> {
                    _reviewList.value = it.result
                    Log.d("ReviewModel", "getReviewList: ${it.result}")
                }

                is Resource.Failure -> {
                    Log.d("OrderViewModel", "getReviewList: Error${it.exception}")
                }

                else -> {}
            }
        }
    }

    private suspend fun getOrders() {
        isLoading.value = true
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repo.getOrdersFlow().collect { orders ->
                _orderList.value = orders.sortedByDescending { it.date }
                val pendingList = mutableListOf<OrderModel>()
                val acceptedList = mutableListOf<OrderModel>()
                val completedList = mutableListOf<OrderModel>()
                val cancelledList = mutableListOf<OrderModel>()
                orderList.value.forEach{
                    order->
                    when(order.orderStatus){
                        OrderStatus.PENDING->{
                            if (order.date >= today) {
                                pendingList.add(order)
                            }else{
                                order.orderStatus=OrderStatus.CANCELLED
                                cancelledList.add(order)
                                viewModelScope.launch {
                                    updateOrderStatus(order.orderId,OrderStatus.CANCELLED.status)
                                }
                            }
                        }
                        OrderStatus.ACCEPTED -> {
                            if (order.date >= today) {
                                acceptedList.add(order)
                            }else{
                                order.orderStatus=OrderStatus.CANCELLED
                                cancelledList.add(order)
                                viewModelScope.launch {
                                    updateOrderStatus(order.orderId,OrderStatus.CANCELLED.status)
                                }
                            }
                        }
                        OrderStatus.COMPLETED -> {
                            completedList.add(order)
                        }
                        else->{
                            cancelledList.add(order)
                        }
                    }
                }
                _acceptedOrderList.value=acceptedList
                _cancelledOrderList.value=cancelledList
                _pendingOrderList.value=pendingList
                _completedOrderList.value=completedList
//                _acceptedOrderList.value = orders.filter {
//                    it.orderStatus == OrderStatus.ACCEPTED && it.date >= LocalDate.now()
//                        .toString()
//                }
//                _pendingOrderList.value = orders.filter {
//                    it.orderStatus == OrderStatus.PENDING && it.date >= LocalDate.now().toString()
//                }
//                _completedOrderList.value =
//                    orders.filter { it.orderStatus == OrderStatus.COMPLETED }
//                _cancelledOrderList.value = orders.filter {
//                    it.orderStatus == OrderStatus.CANCELLED || (it.date < LocalDate.now()
//                        .toString() && it.orderStatus != OrderStatus.COMPLETED)
//                }
                upcomingOrder.value = _acceptedOrderList.value.firstOrNull() ?: OrderModel(
                    barberShopName = " ",
                    barberName = " ",
                    imageUrl = " ",
                    orderType = emptyList(),
                    phoneNumber = " ",
                    timeSlot = emptyList(),
                )
                isLoading.value = false

                getReviewList() // Launching the review fetch without another coroutine scope
            }
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        repo.updateOrderStatus(orderId, status).collect {
            when (it) {
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