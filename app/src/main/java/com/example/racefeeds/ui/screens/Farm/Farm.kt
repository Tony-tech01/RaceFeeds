package com.example.racefeeds.ui.screens.Farm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.racefeeds.ui.Components.CartIconWithBadge
import com.example.racefeeds.ui.Components.ToolCard
import com.example.racefeeds.ui.Components.TopBarWithCart
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import com.example.racefeeds.ui.Components.ScreenTitleWithCart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmScreen(
    viewModel: FarmSupplyViewModel = viewModel(),
    cartViewModel: CartViewModel,
    navController: NavController,
    onNavigateToCart: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartCount by cartViewModel.cartCount.collectAsState()

    Scaffold(
         topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Race Feeds") },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            ScreenTitleWithCart(
                title = "Farm Supplies",
                cartCount = cartCount,
                onNavigateToCart = onNavigateToCart
            )

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearch,
                placeholder = { Text("Search tools...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(uiState.categories) { index, category ->
                    val isSelected = category.name == uiState.selectedCategory
                    Text(
                        text = category.name,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(
                                if (isSelected) Color(0xFFE0F2F1) else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.onCategorySelected(category.name) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.tools.size) { index ->
                    val tool = uiState.tools[index]
                    ToolCard(tool = tool, onClick = {})
                }
            }
        }
    }
}