package com.example.racefeeds.ui.screens.OrderHistory

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderRepository {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun addOrder(order: Order) {
        _orders.value = _orders.value + order
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
    }

    fun updateTracking(orderId: String, trackingInfo: TrackingInfo) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(trackingInfo = trackingInfo) else it
        }
    }
}