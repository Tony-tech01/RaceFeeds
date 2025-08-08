package com.example.racefeeds.ui.screens.Farm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.racefeeds.R
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmToolDetailsScreen(
    toolId: String,
    farmSupplyViewModel: FarmSupplyViewModel = remember { FarmSupplyViewModel() },
    cartViewModel: CartViewModel = remember { CartViewModel() },
    navController: NavController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()




    LaunchedEffect(toolId) {
        farmSupplyViewModel.loadToolById(toolId)
    }

    val tool by farmSupplyViewModel.toolDetailState.collectAsState()

    Scaffold(
        topBar = {
        TopAppBar(title = { Text("Tool Details") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->

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
                val painter = runCatching {
                    painterResource(id = currentTool.imageRes)
                }.getOrElse {
                    painterResource(id = R.drawable.ic_launcher_foreground)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = currentTool.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(text = currentTool.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Category: ${currentTool.category}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = currentTool.description, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "KSh ${currentTool.price}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = {
                            cartViewModel.addToCart(
                                itemName = currentTool.name, price = currentTool.price
                            )
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${currentTool.name} added to cart",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add to Cart")
                    }

                }
            }
        }
    }
}