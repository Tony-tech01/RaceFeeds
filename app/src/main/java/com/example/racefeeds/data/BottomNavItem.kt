package com.example.racefeeds.data

import androidx.annotation.DrawableRes
import com.example.racefeeds.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconRes: Int,
    val label: String
) {
    object Home : BottomNavItem(
        route = "home",
        iconRes = R.drawable.home,
        label = "Home"
    )
    object Feed : BottomNavItem(
        route = "feeds",
        iconRes = R.drawable.food,
        label = "Feeds"
    )
    object Tool : BottomNavItem(
        route = "farm",
        iconRes =  R.drawable.agriculture,
        label = "Tools"
    )
    object Cart: BottomNavItem(
        route = "cart",
        iconRes= R.drawable.cart,
        label = "Cart"
    )

}