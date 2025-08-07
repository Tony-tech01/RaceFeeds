package com.example.racefeeds.ui.screens.Checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.ui.screens.Checkout.CheckoutUiState
import com.example.racefeeds.ui.screens.Cart.CartItem
import com.example.racefeeds.ui.screens.Checkout.PaymentOption
import com.example.racefeeds.ui.screens.OrderHistory.Order
import com.example.racefeeds.ui.screens.OrderHistory.OrderRepository
import com.example.racefeeds.ui.screens.OrderHistory.OrderStatus
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime


class CheckoutViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun setCartItems(items: List<CartItem>) {
        _uiState.update {
            it.copy(cartItems = items)
        }
    }



    fun onLocationChanged(newLocation: String) {
        _uiState.update { it.copy(location = newLocation) }
    }

    fun onPaymentOptionSelected(option: PaymentOption) {
        _uiState.update { it.copy(paymentOption = option) }
    }

    fun onPhoneNumberChanged(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                errorMessage = if (phone.isNotEmpty() && !phone.matches(Regex("^07\\d{8}$"))) {
                    "Invalid phone number format"
                } else {
                    null
                }
            )
        }
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
            showError("Please choose a payment option")
            return
        }


        if (state.paymentOption == PaymentOption.MPESA && state.phoneNumber.isBlank()) {
            showError("Please enter a valid phone number for M-Pesa")
            return
        }


        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true, errorMessage = null) }

            delay(2000)


            val newOrder = Order(
                id = generateOrderId(),
                items = state.cartItems,
                status = OrderStatus.PENDING,
                date = LocalDateTime.now(),
                trackingInfo = null
            )

            orderRepository.addOrder(newOrder)


            _uiState.update {
                it.copy(
                    isOrderPlaced = true,
                    showConfirmationDialog = true,
                    isProcessingPayment = false,
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


    fun confirmOrder() {
        _uiState.update { it.copy(isOrderPlaced = true, showConfirmationDialog = false) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmationDialog = false) }
    }

}
