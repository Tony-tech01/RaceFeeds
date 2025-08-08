//package com.example.racefeeds.ui.screens.settings
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.racefeeds.ui.screens.firebase.AuthViewModel
//
//@Composable
//fun ManageAccountScreen(
//    authViewModel: AuthViewModel,
//    navController: NavController
//) {
//    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text("Manage Account", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        if (isAuthenticated) {
//           // Text("You're logged in as: ${authViewModel.currentUserEmail ?: "Unknown"}")
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            TextButton(onClick = {
////                authViewModel.logout()
//                navController.navigate("login") {
//                    popUpTo("manage_account") { inclusive = true }
//                }
//            }) {
//                Text("Log Out")
//            }
//
//            // Add more actions: Change password, delete account, etc.
//        } else {
//            Text("You're not logged in.")
//            TextButton(onClick = {
//                navController.navigate("login?returnTo=manage_account")
//            }) {
//                Text("Log In")
//            }
//        }
//    }
//}