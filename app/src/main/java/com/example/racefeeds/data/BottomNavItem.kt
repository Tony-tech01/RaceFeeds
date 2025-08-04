package com.example.racefeeds.data

import androidx.annotation.DrawableRes
import com.example.racefeeds.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconRes: Int,
    val label: String
) {
    object Feed : BottomNavItem(
        route = "feeds",
        iconRes = R.drawable.food,
        label = "Feeds"
    )
    object Farm : BottomNavItem(
        route = "farm",
        iconRes =  R.drawable.agriculture,
        label = "Animal"
    )
    object Cart: BottomNavItem(
        route = "cart",
        iconRes= R.drawable.cart,
        label = "Cart"
    )

}