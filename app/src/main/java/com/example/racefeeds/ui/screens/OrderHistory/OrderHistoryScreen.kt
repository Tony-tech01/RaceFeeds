package com.example.racefeeds.ui.screens.OrderHistory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun OrderHistoryScreen(
    navController: NavController,
    orderHistoryViewModel: OrderHistoryViewModel,
    innerPadding: PaddingValues,
) {
    val orders by orderHistoryViewModel.orders.collectAsState()
    val categories = listOf("Pending", "Completed", "Cancelled")
    var selectedCategory by rememberSaveable { mutableStateOf("Pending") }

    val filteredOrders = orders.filter {
        it.status.name.equals(selectedCategory, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        CategoryTabRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it })

        Spacer(modifier = Modifier.height(8.dp))

        if (orders.isEmpty()) {
            EmptyState("No orders found.")
        } else if (filteredOrders.isEmpty()) {
            EmptyState("No ${selectedCategory.lowercase()} orders found.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
            ) {
                itemsIndexed(filteredOrders) { _, order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
private fun CategoryTabRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    val categoryIcons = mapOf(
        "Pending" to Icons.Default.Schedule,
        "Completed" to Icons.Default.CheckCircle,
        "Cancelled" to Icons.Default.Cancel
    )

    TabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[categories.indexOf(
                        selectedCategory
                    )]
                ), color = MaterialTheme.colorScheme.primary, height = 3.dp
            )
        }) {
        categories.forEach { category ->
            Tab(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = categoryIcons[category] ?: Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(category, style = MaterialTheme.typography.labelLarge)
                    }
                })
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun OrderCard(order: Order) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    var expanded by remember { mutableStateOf(false) }
    val totalPrice = order.items.sumOf { it.price * it.quantity }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${order.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.date?.format(dateFormatter) ?: "Unknown",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Status: ${
                    order.status.name.lowercase().replaceFirstChar { it.uppercase() }
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total: KSh ${"%,.2f".format(totalPrice)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )


            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Items Purchased:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    order.items.forEach { item ->
                        Text(
                            text = "- ${item.name} × ${item.quantity}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

}