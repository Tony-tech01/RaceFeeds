package com.example.racefeeds.ui.screens.OrderHistory

import Order
import OrderStatus
import TrackingInfo
import androidx.lifecycle.ViewModel
import com.example.racefeeds.data.repository.OrderRepository

import kotlinx.coroutines.flow.StateFlow

class OrderHistoryViewModel(private val repository: OrderRepository) : ViewModel() {
    val orders: StateFlow<List<Order>> = repository.orders

    fun addOrder(order: Order) = repository.addOrder(order)

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) =
        repository.updateOrderStatus(orderId, newStatus)

    fun updateTracking(orderId: String, trackingInfo: TrackingInfo) =
        repository.updateTracking(orderId, trackingInfo)

    fun loadOrdersForUser(userId: String) {
        repository.fetchOrdersForUser(userId)
    }

    fun loadAllOrdersForAdmin() {
        repository.loadAllOrdersForAdmin()
    }

}