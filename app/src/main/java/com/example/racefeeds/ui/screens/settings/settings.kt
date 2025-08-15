package com.example.racefeeds.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.racefeeds.data.BottomNavItem
import com.example.racefeeds.ui.screens.firebase.AuthViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineMedium)
            ListItem(headlineContent = { Text("Home") }, leadingContent = {
                Icon(
                    imageVector = Icons.Default.Home, contentDescription = "Home"
                )
            }, modifier = Modifier.clickable {
                navController.navigate(BottomNavItem.Feed.route)
            })

        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode", modifier = Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = settingsViewModel::toggleDarkMode)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Notifications", modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = settingsViewModel::toggleNotifications
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ListItem(headlineContent = { Text("Order History") }, leadingContent = {
            Icon(
                imageVector = Icons.Default.List, contentDescription = "Order History"
            )
        }, modifier = Modifier.clickable {
            navController.navigate("order_history")
        })

        ListItem(headlineContent = { Text("Admin Dashboard") }, leadingContent = {
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin Panel Settings"
            )
        }, modifier = Modifier.clickable {
            if (isAdmin) {
                navController.navigate("admin")
            } else {
                Toast.makeText(context, "Access denied: Admins only", Toast.LENGTH_SHORT).show()
            }


        })

        Spacer(modifier = Modifier.height(32.dp))

        Text("Authentication", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val userEmail = authViewModel.currentUser.collectAsState().value?.email
        Text(
            text = if (userEmail != null) "Signed in as: $userEmail" else "Not signed in",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isAuthenticated) {
            Button(
                onClick = {
                    authViewModel.signOut()
                    navController.navigate("login?returnTo=settings")
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        navController.navigate("login?returnTo=settings")
                    }, modifier = Modifier.weight(1f)
                ) {
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        navController.navigate("signup?returnTo=settings")
                    }, modifier = Modifier.weight(1f)
                ) {
                    Text("Sign Up")
                }
            }
        }
    }
}