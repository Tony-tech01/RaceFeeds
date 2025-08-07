package com.example.racefeeds.ui.screens.OrderHistory

import com.example.racefeeds.ui.screens.Cart.CartItem
import org.threeten.bp.LocalDateTime


data class Order(
    val id: String,
    val items: List<CartItem>,
    val status: OrderStatus,
    val date: LocalDateTime,
    val trackingInfo: TrackingInfo? = null
)

enum class OrderStatus {
    PENDING, COMPLETED, CANCELLED
}

data class TrackingInfo(
    val currentLocation: String,
    val estimatedDelivery: LocalDateTime,
    val progress: Float
)
