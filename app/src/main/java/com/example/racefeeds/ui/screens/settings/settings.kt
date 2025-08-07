package com.example.racefeeds.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, innerPadding: PaddingValues,navController: NavController ) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(start = 8.dp, end = 8.dp)
    ) {

        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode", modifier = Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = settingsViewModel::toggleDarkMode)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Account", modifier = Modifier.weight(1f))
            TextButton(onClick = { }) {
                Text("Manage")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Notifications", modifier = Modifier.weight(1f))
            Switch(checked = notificationsEnabled, onCheckedChange = settingsViewModel::toggleNotifications)
        }
        ListItem(
            headlineContent = { Text("Order History") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Order History"
                )
            },
            modifier = Modifier.clickable {
                navController.navigate("order_history")
            }
        )


    }
}