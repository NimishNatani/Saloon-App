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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private val _orderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val orderList: StateFlow<MutableList<OrderModel>> = _orderList.asStateFlow()
    private val _acceptedOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val acceptedOrderList: StateFlow<MutableList<OrderModel>> = _acceptedOrderList.asStateFlow()
    private val _pendingOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val pendingOrderList: StateFlow<MutableList<OrderModel>> = _pendingOrderList.asStateFlow()
    private val _completedOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val completedOrderList: StateFlow<MutableList<OrderModel>> = _completedOrderList.asStateFlow()
    private val _cancelledOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val cancelledOrderList: StateFlow<MutableList<OrderModel>> = _cancelledOrderList.asStateFlow()

    var isLoading = mutableStateOf(false)
    var isUpdating = mutableStateOf(false)

    private val _reviewList = mutableStateOf<List<ReviewModel>>(emptyList())
    val reviewList: State<List<ReviewModel>> = _reviewList

//    var upcomingOrder = mutableStateOf(
//        OrderModel(
//            barberShopName = " ",
//            barberName = " ",
//            imageUrl = " ",
//            orderType = emptyList(),
//            phoneNumber = " ",
//            timeSlot = emptyList(),
//        )
//    )

    init {
        viewModelScope.launch {
            getOrders()
        }
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
        isUpdating.value = true
        repo.addReview(orderId, review).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("rOrderViewModel", "addReview: Success")
                    isUpdating.value = false
                    onCompletion()
                }

                is Resource.Failure -> {
                    isUpdating.value = false
                    Log.d("rOrderViewModel", "addReview: Error ${it.exception}")
                }

                else -> {}
            }
        }
    }

    fun getReviewByOrderId(orderId: String): ReviewModel? {
        return _reviewList.value.find { it.orderId == orderId }
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
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repo.getOrder().collect { orders ->
                _orderList.emit(orders.toMutableList())
                _pendingOrderList.update { it.toMutableList().apply { clear() } }
                _acceptedOrderList.update { it.toMutableList().apply { clear() } }
                _completedOrderList.update { it.toMutableList().apply { clear() } }
                _cancelledOrderList.update { it.toMutableList().apply { clear() } }
                orders.forEach { order ->
                    Log.d("OrderViewModel", "getOrders: ${order.orderStatus}")

                    when (order.orderStatus) {
                        OrderStatus.PENDING -> {
                            if (order.date >= today) {
                                _pendingOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                            } else {
                                order.orderStatus = OrderStatus.CANCELLED
                                _cancelledOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                updateOrderStatus(order, OrderStatus.CANCELLED.status)
                            }
                        }

                        OrderStatus.ACCEPTED -> {
                            if (order.date >= today) {
                                _acceptedOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                            } else {
                                order.orderStatus = OrderStatus.CANCELLED
                                _cancelledOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                updateOrderStatus(order, OrderStatus.CANCELLED.status)
                            }
                        }

                        OrderStatus.COMPLETED -> {
                            _completedOrderList.update {
                                it.toMutableList().apply {
                                    add(order)
                                }
                            }
                        }

                        OrderStatus.CANCELLED -> {
                            _cancelledOrderList.update {
                                it.toMutableList().apply {
                                    add(order)
                                }
                            }
                        }
                    }
                    Log.d("size",_pendingOrderList.value.size.toString()+_cancelledOrderList.value.size.toString()+_acceptedOrderList.value.size.toString()+_completedOrderList.value.size.toString())
                }
            }

        }

    }

    suspend fun updateOrderStatus(order: OrderModel, status: String) {

        viewModelScope.launch {
            isUpdating.value=true
            repo.updateOrderStatus(order, status).collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d("OrderViewModel", "updateOrderStatus: Success")
                        Log.d("OrderViewModel", "updateOrderStatus: ${LocalDate.now()}")
                        isUpdating.value=false
//                        getOrders()
                    }

                    is Resource.Failure -> {
                        Log.d("OrderViewModel", "updateOrderStatus: Error")
                        isUpdating.value=false
                    }

                    else -> {}
                }
            }

        }

    }
}

sealed class OrderEvent {
    object GetOrderList : OrderEvent()
    object UpdateOrderStatus : OrderEvent()
    object AddReview : OrderEvent()
}