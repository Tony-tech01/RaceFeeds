package com.example.racefeeds.ui.screens.Cart

data class CartItem(
    val name: String,
    val price: Double,
    var quantity: Int = 1
)