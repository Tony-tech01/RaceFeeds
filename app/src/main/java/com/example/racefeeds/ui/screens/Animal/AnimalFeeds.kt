package com.example.racefeeds.ui.screens.Animal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.racefeeds.R
import com.example.racefeeds.ui.Components.HeadWithSeeAll
import com.example.racefeeds.ui.Components.ScreenTitleWithCart
import com.example.racefeeds.ui.Components.SearchBar
import com.example.racefeeds.ui.screens.Cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmPage(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    onNavigateToCart: () -> Unit,
    navController: NavController,
) {
    val animalViewModel: AnimalViewModel = viewModel()
    val uiState by animalViewModel.uiState.collectAsState()

    val cartCount by cartViewModel.cartCount.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                Text(
                    "Race Feeds", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp
                    )
                )
            },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.deep_green),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ScreenTitleWithCart(
                    title = "Animal Feeds",
                    cartCount = cartCount,
                    onNavigateToCart = onNavigateToCart
                )
            }

            item {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = animalViewModel::onSearchQueryChanged,
                    placeholder = "Search for animal feeds",
                    onSearchTriggered = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
            }


            if (uiState.matchingFeedsWithContext.isNotEmpty()) {
                item {
                    Text(
                        text = "Search Results:",
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }


                items(uiState.matchingFeedsWithContext) { context ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .clickable {
                                animalViewModel.onFeedSelected(
                                    feedItem = context.feedItem,
                                    breed = context.breed,
                                    animal = context.animal
                                )
                            }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Feed: ${context.feedItem.name}")
                                Text("Breed: ${context.breed?.name ?: "General"}")
                                Text("Animal: ${context.animal.name}")
                            }
                            Text(
                                "Price: KES ${context.feedItem.price}"
                            )
                        }
                    }
                }

            }

            item {
                HeadWithSeeAll(title = "Animal Categories")
            }




            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.displayedAnimals) { animal ->
                        val isSelected = animal == uiState.selectedAnimal
//                        val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1f)

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                    else Color.LightGray.copy(alpha = 0.3f)
                                )
                               .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) colorResource(id = R.color.deep_green) else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { animalViewModel.onAnimalSelected(animal) }
                                .padding(8.dp),

                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                            ) {
                            Image(
                                painter = painterResource(id = animal.image),
                                contentDescription = animal.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                            Text(
                                text = animal.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                }
            }






            uiState.selectedAnimal?.let { animal ->
                if (animal.breeds.isNotEmpty()) {
                    item {
                        Text(
                            text = "Select Breed:",
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(animal.breeds) { breed ->
                                val isSelected = breed == uiState.selectedBreed
                                if (isSelected) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    Color.Transparent
                                }

                                Card(
                                    modifier = Modifier.clickable {
                                        animalViewModel.onBreedSelected(breed)
                                    }
                                        .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) colorResource(id = R.color.deep_green) else Color.Transparent,
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                        else MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = if (isSelected) 6.dp else 2.dp
                                    )
                                ) {

                                    Text(
                                        text = breed.name,
                                        modifier = Modifier.padding(12.dp),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }

                        }
                    }


                }
            }




            uiState.foodInfo?.let { foodInfo ->

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = uiState.selectedBreed?.let { "Food Info for ${it.name}" }
                            ?: uiState.selectedAnimal?.let { "Food Info for ${it.name}" }
                            ?: "Food Info", style = MaterialTheme.typography.titleLarge)


                        Button(
                            onClick = { animalViewModel.closeFoodInfo() },
                        ) {
                            Text(text = "Close")
                        }
                    }
                }

                items(foodInfo.items) { foodItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = foodItem.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Price: KES ${foodItem.price} per Kg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = {
                                    cartViewModel.addToCart(
                                        foodItem.name, foodItem.price
                                    )
                                }, shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.orange),
                                    contentColor = Color.White
                                )
                            ) {
                                Row{
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Text("Add")
                                }

                            }
                        }
                    }

                }

            }



            uiState.errorMessage?.let { error ->
                item {
                    LaunchedEffect(error) {
                        kotlinx.coroutines.delay(3000)
                        animalViewModel.clearErrorMessage()
                    }
                    Text(
                        text = error, color = Color.Red, modifier = Modifier.padding(16.dp)
                    )
                }

            }
        }
    }
}
