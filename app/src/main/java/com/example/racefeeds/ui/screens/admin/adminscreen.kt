package com.example.racefeeds.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.racefeeds.ui.screens.OrderHistory.OrderHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminScreen(
    orderHistoryViewModel: OrderHistoryViewModel,
    navController: NavController,
    innerPadding: PaddingValues
) {
    LaunchedEffect(Unit) {
        orderHistoryViewModel.loadAllOrdersForAdmin()
    }

    val orders by orderHistoryViewModel.orders.collectAsState()

    Column(modifier = Modifier.padding(innerPadding).padding(8.dp)) {
        Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        orders.forEach { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Order ID: ${order.orderId}")
                    Text("User ID: ${order.userId}")
                    Text("Status: ${order.status}")

                    val formattedDate = formatDateString(order.date)
                    Text("Date: $formattedDate")

                    order.trackingInfo?.let {
                        Text("Tracking: ${it.currentLocation}, ETA: ${it.estimatedDelivery}")
                    }

                    Button(onClick = {
                        navController.navigate("order_detail/${order.orderId}")
                    }) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}

private fun formatDateString(timestamp: com.google.firebase.Timestamp?): String {
    return try {
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()
        outputFormat.format(timestamp?.toDate())
    } catch (e: Exception) {
        "Invalid date"
    }
}