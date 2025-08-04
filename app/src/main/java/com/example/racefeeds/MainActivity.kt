package com.example.racefeeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.racefeeds.data.BottomNavItem
import com.example.racefeeds.ui.Components.AppNavGraph
import com.example.racefeeds.ui.Components.BottomBar
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import com.example.racefeeds.ui.screens.Checkout.CheckoutViewModel
import com.example.racefeeds.ui.theme.RaceFeedsTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val cartViewModel: CartViewModel by viewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels() // ✅ Added

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaceFeedsTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentDestination?.route in listOf(
                                BottomNavItem.Feed.route,
                                BottomNavItem.Farm.route,
                                BottomNavItem.Cart.route
                            )
                        ) {
                            BottomBar(
                                navController = navController,
                                currentDestination = currentDestination
                            )
                        }
                    }
                ) { contentPadding ->
                    AppNavGraph(
                        navController = navController,
                        cartViewModel = cartViewModel,
                        checkoutViewModel = checkoutViewModel,
                        contentPadding = contentPadding,
                        onNavigateToCheckout = {
                            checkoutViewModel.setCartItems(cartViewModel.cartItems.value)
                            navController.navigate("checkout")
                        }
                    )
                }
            }
        }
    }
}
