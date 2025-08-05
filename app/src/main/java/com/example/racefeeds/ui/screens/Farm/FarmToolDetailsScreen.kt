package com.example.racefeeds.ui.screens.Farm

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.racefeeds.R
import com.example.racefeeds.ui.Components.ToolCard
import com.example.racefeeds.ui.screens.Cart.CartViewModel


@Composable
fun FarmToolDetailsScreen(
    toolId: String,
    farmSupplyViewModel: FarmSupplyViewModel = remember { FarmSupplyViewModel() },
    cartViewModel: CartViewModel = remember { CartViewModel() },
    navController: NavController
) {
    LaunchedEffect(toolId) {
        farmSupplyViewModel.loadToolById(toolId)
    }

    val context = LocalContext.current
    val tool by farmSupplyViewModel.toolDetailState.collectAsState()
    val uiState by farmSupplyViewModel.uiState.collectAsState()

    Scaffold(contentWindowInsets = WindowInsets.systemBars) { innerPadding ->
        when (tool) {
            is ToolUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ToolUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (tool as ToolUiState.Error).message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is ToolUiState.Success -> {
                val currentTool = (tool as ToolUiState.Success).tool




                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Image(
                            painter = runCatching {
                                painterResource(id = currentTool.imageRes)
                            }.getOrElse {
                                painterResource(id = R.drawable.ic_launcher_foreground) // fallback
                            },
                            contentDescription = currentTool.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = currentTool.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Category: ${currentTool.category}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = currentTool.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "KSh ${currentTool.price}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Button(
                            onClick = {
                                cartViewModel.addToCart(
                                    itemName = currentTool.name,
                                    price = currentTool.price
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to Cart")
                        }

                        Text(
                            text = "More Tools",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.tools.size) { index ->
                                val farmTool = uiState.tools[index]
                                ToolCard(
                                    tool = farmTool,
                                    onClick = {
                                        Log.d("ToolNav", "Navigating to: ${farmTool.id}")
                                        navController.navigate("farmToolDetails/${farmTool.id}")
                                    },
                                    cartViewModel = cartViewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}