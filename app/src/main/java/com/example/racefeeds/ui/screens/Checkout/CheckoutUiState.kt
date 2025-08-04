package com.example.racefeeds.ui.screens.Checkout

import com.example.racefeeds.ui.screens.Cart.CartItem

data class CheckoutUiState(
    val cartItems: List<CartItem> = emptyList(),
    val location: String = "",
    val paymentOption: PaymentOption? = null,
    val phoneNumber: String = "",
    val errorMessage: String? = null,
    val showConfirmationDialog: Boolean = false,
    val isOrderPlaced: Boolean = false,
    val isWaitingForPayment: Boolean = false,
    val isProcessingPayment: Boolean = false


){
    val totalPrice: Double
        get() = cartItems.sumOf { it.price * it.quantity }
}