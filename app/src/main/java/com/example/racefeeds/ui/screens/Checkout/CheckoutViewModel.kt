package com.example.racefeeds.ui.screens.Checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.ui.screens.Checkout.CheckoutUiState
import com.example.racefeeds.ui.screens.Cart.CartItem
import com.example.racefeeds.ui.screens.Checkout.PaymentOption
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CheckoutViewModel : ViewModel() {

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


    fun placeOrder() {
        val state = _uiState.value


        if (state.location.isBlank() || state.paymentOption == null) {
            _uiState.update {
                it.copy(errorMessage = "Please fill in location and choose a payment option")
            }
            return
        }


        if (state.paymentOption == PaymentOption.MPESA && state.phoneNumber.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter a valid phone number for M-Pesa")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true, errorMessage = null) }

            delay(5000)

            _uiState.update {
                it.copy(
                    isOrderPlaced = true,
                    showConfirmationDialog = true,
                    isProcessingPayment = false
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
