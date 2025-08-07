package com.example.racefeeds.ui.screens.Cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.ui.screens.Cart.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CartViewModel: ViewModel()  {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()


    fun addToCart(itemName: String, price: Double) {
        Log.d("CartDebug", "Adding item to cart: $itemName")
        _cartItems.update { currentItems ->
            val existing = currentItems.find { it.name == itemName }
            if (existing != null) {
                currentItems.map {
                    if (it.name == itemName) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(name = itemName, price = price)
            }
        }
        Log.d("CartDebug", "Cart items: ${_cartItems.value}")
    }

    val cartCount = _cartItems
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), 0)

    fun decreaseQuantity(itemName: String) {
        _cartItems.update{ currentItems ->
            currentItems.mapNotNull{
                if (it.name == itemName && it.quantity > 1) {
                    it.copy(quantity = it.quantity - 1)
                } else if (it.name == itemName && it.quantity == 1) {
                    null
                } else {
                    it
                }
            }

        }
    }

    fun removeFromCart(itemName: String) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.name == itemName }
        }

    }
    fun clearCart() {
        _cartItems.update { emptyList() }
    }


}