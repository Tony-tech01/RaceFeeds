package com.example.racefeeds.ui.screens.Farm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.racefeeds.ui.Components.ScreenTitleWithCart
import com.example.racefeeds.ui.Components.ToolCard
import com.example.racefeeds.ui.screens.Cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmScreen(
    farmSupplyViewModel: FarmSupplyViewModel = viewModel(),
    cartViewModel: CartViewModel,
    navController: NavController,
    onNavigateToCart: () -> Unit,
) {
    val uiState by farmSupplyViewModel.uiState.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()

    Scaffold { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 🔹 Header (full width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                ScreenTitleWithCart(
                    title = "Farm Supplies",
                    cartCount = cartCount,
                    onNavigateToCart = onNavigateToCart
                )
            }

            // 🔹 Search (full width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = farmSupplyViewModel::onSearch,
                    placeholder = { Text("Search tools...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            // 🔹 Categories (full width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(uiState.categories) { _, category ->
                        val isSelected = category.name == uiState.selectedCategory

                        Text(
                            text = category.name,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .background(
                                    if (isSelected)
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    farmSupplyViewModel.onCategorySelected(category.name)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // 🔥 Grid items (normal)
            items(uiState.tools.size) { index ->
                val tool = uiState.tools[index]

                ToolCard(
                    tool = tool,
                    onClick = { navController.navigate("farmToolDetails/${tool.id}") },
                    cartViewModel = cartViewModel,
                    navController = navController
                )
            }
        }
    }
}