package com.example.racefeeds.ui.screens.Cart

import android.util.Log

data class CartItem(
    val name: String = "",
    val price: Double = 0.0,
    var quantity: Int = 1
){
    companion object {
        fun fromMap(map: Map<String, Any>): CartItem? {
            return try {
                CartItem(
                    name = map["name"] as? String ?: "",
                    quantity = (map["quantity"] as? Long)?.toInt() ?: 0,
                    price = map["price"] as? Double ?: 0.0
                )
            } catch (e: Exception) {
                Log.e("CartItem", "Failed to parse cart item", e)
                null
            }
        }
    }
}
