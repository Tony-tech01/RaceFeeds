package com.example.racefeeds.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.racefeeds.ui.screens.Checkout.CheckoutViewModel
import com.example.racefeeds.ui.screens.OrderHistory.OrderHistoryViewModel
import com.example.racefeeds.ui.screens.OrderHistory.OrderRepository

class SharedViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OrderHistoryViewModel::class.java) ->
                OrderHistoryViewModel(orderRepository) as T
            modelClass.isAssignableFrom(CheckoutViewModel::class.java) ->
                CheckoutViewModel(orderRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}