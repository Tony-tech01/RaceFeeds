package com.example.racefeeds


import com.example.racefeeds.data.SharedViewModelFactory
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.racefeeds.data.BottomNavItem
import com.example.racefeeds.ui.Components.AppNavGraph
import com.example.racefeeds.ui.Components.BottomBar
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import com.example.racefeeds.ui.screens.Checkout.CheckoutViewModel
import com.example.racefeeds.ui.screens.OrderHistory.OrderHistoryViewModel
import com.example.racefeeds.ui.screens.OrderHistory.OrderRepository
import com.example.racefeeds.ui.screens.firebase.AuthViewModel
import com.example.racefeeds.ui.screens.firebase.AuthViewModelFactory
import com.example.racefeeds.ui.screens.settings.SettingsViewModel
import com.example.racefeeds.ui.theme.RaceFeedsTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val cartViewModel: CartViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        Log.d("Firebase", "Initialized: ${FirebaseApp.getInstance().name}")
        val splashScreen = installSplashScreen()


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        var keepSplashVisible = true
        splashScreen.setKeepOnScreenCondition { keepSplashVisible }
        lifecycleScope.launch {
            delay(3000)
            keepSplashVisible = false
        }
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(FirebaseAuth.getInstance())
        )[AuthViewModel::class.java]

        setContent {

            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                Log.d("MainActivity", "Navigating to FarmScreen")
                navController.navigate(BottomNavItem.Feed.route)
            }
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry.value?.destination
            val orderRepository = remember { OrderRepository() }

            val sharedFactory = remember { SharedViewModelFactory(orderRepository) }
            val checkoutViewModel: CheckoutViewModel = viewModel(factory = sharedFactory)
            val orderHistoryViewModel: OrderHistoryViewModel = viewModel(factory = sharedFactory)
            val view = LocalView.current
            val window = (view.context as Activity).window
            SideEffect {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()

                WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = !isDarkMode
                WindowInsetsControllerCompat(window, view).isAppearanceLightNavigationBars =
                    !isDarkMode
            }

            RaceFeedsTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), bottomBar = {
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
                    }) { contentPadding ->
                    AppNavGraph(
                        navController = navController,
                        cartViewModel = cartViewModel,
                        authViewModel = authViewModel,
                        checkoutViewModel = checkoutViewModel,
                        contentPadding = contentPadding,
                        onNavigateToCheckout = {
                            checkoutViewModel.setCartItems(cartViewModel.cartItems.value)
                            val isAuthenticated = authViewModel.isAuthenticated.value
                            if (isAuthenticated) {
                                navController.navigate("checkout")
                            } else {
                                navController.navigate("login?returnTo=checkout")
                            }
                        },
                        settingsViewModel = settingsViewModel,
                        orderHistoryViewModel = orderHistoryViewModel,

                    )
                }
            }
        }
    }
}