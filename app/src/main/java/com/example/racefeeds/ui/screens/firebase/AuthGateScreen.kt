package com.example.racefeeds.ui.screens.firebase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.racefeeds.data.BottomNavItem
import androidx.compose.runtime.getValue

@Composable
fun AuthGateScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        val target = if (isAuthenticated) BottomNavItem.Feed.route else "login?returnTo=feeds"
        android.util.Log.d("AuthGate", "Routing to: $target")
        navController.navigate(target) {
            popUpTo("authGate") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}