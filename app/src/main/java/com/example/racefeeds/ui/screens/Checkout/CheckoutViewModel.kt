package com.example.racefeeds.ui.screens.Checkout


import Order

import OrderStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.data.repository.OrderRepository
import com.example.racefeeds.ui.screens.Cart.CartItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun setCartItems(items: List<CartItem>) {
        _uiState.update { it.copy(cartItems = items) }
    }

    fun onLocationChanged(newLocation: String) {
        _uiState.update { it.copy(location = newLocation) }
    }

    fun onPaymentOptionSelected(option: PaymentOption?) {
        _uiState.update { it.copy(paymentOption = option) }
    }

    fun setUserEmail(email: String) {
        _uiState.update { it.copy(userEmail = email) }
    }

    private fun generateOrderId(): String {
        return "ORD-${System.currentTimeMillis()}"
    }

    fun placeOrder() {
        val state = _uiState.value

        if (state.location.isBlank()) {
            showError("Please enter a delivery location")
            return
        }

        if (state.paymentOption == null) {
            showError("Please select a payment option")
            return
        }



        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isProcessingPayment = true, errorMessage = null, showConfirmationDialog = true
                )
            }

            kotlinx.coroutines.delay(1000)

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val newOrder = Order(
                orderId = generateOrderId(),
                userId = uid,
                userEmail = state.userEmail,
                items = state.cartItems,
                status = OrderStatus.PENDING,
                date = getFormattedDate(),
                trackingInfo = null
            )

            orderRepository.addOrder(newOrder)

            _uiState.update {
                it.copy(
                    isOrderPlaced = true,
                    showConfirmationDialog = false,
                    isProcessingPayment = false,
                    cartItems = emptyList(),
                    location = "",
                    paymentOption = null
                )
            }
        }
    }

    fun getFormattedDate(): Timestamp {
        return Timestamp.now()
    }


    fun confirmOrder() {
        val state = _uiState.value

        if (state.userEmail.isBlank()) {
            showError("User email is missing")
            return
        }



        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val newOrder = Order(
                orderId = generateOrderId(),
                userId = uid,
                userEmail = state.userEmail,
                items = state.cartItems,
                status = OrderStatus.PENDING,
                date = getFormattedDate(),
                trackingInfo = null
            )


            orderRepository.addOrder(newOrder)

            _uiState.update {
                it.copy(
                    isOrderPlaced = true,
                    showConfirmationDialog = false,
                    isWaitingForPayment = false,
                    cartItems = emptyList(),
                    location = "",
                    phoneNumber = "",
                    paymentOption = null
                )
            }
        }
    }

    fun showError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetOrderState() {
        _uiState.update { it.copy(isOrderPlaced = false) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmationDialog = false) }
    }
}