package com.example.racefeeds.ui.screens.Farm

import android.graphics.Color.alpha
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racefeeds.data.AnimalData.animals
import com.example.racefeeds.ui.HeadWithSeeAll
import com.example.racefeeds.ui.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmPage(modifier: Modifier = Modifier, innerPadding: PaddingValues) {
    val farmViewModel: FarmViewModel = viewModel()
    val uiState by farmViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier, topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Race Feeds") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.statusBarsPadding(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Text(
                text = "Animal Feeds",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.headlineLarge
            )

            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = farmViewModel::onSearchQueryChanged,
                placeholder = "Search for animal feeds",
                onSearchTriggered = {})

            HeadWithSeeAll(title = "Animal Categories")

            Spacer(modifier = Modifier.height(4.dp))


            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.displayedAnimals.size) { animal ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))

                            .clickable {
                            farmViewModel.onAnimalSelected(animals[animal])
                        }
                            .padding(8.dp)
                            ,
                    ) {
                        Image(
                            painter = painterResource(id = animals[animal].image),
                            contentDescription = animals[animal].name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(14.dp))

                        )
                        Text(
                            text = animals[animal].name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            uiState.selectedAnimal?.let { animal ->
                if (animal.breeds.isNotEmpty()) {
                    Text(
                        text = "Select Breed:",
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(animal.breeds.size) { breed ->
                            Card(modifier = Modifier
                                .clickable { farmViewModel.onBreedSelected(animal.breeds[breed]) }
                                .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))) {
                                Text(
                                    text = animal.breeds[breed].name,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            uiState.foodInfo?.let { foodInfo ->
                Text(
                    text = "Food Info for ${uiState.selectedBreed?.name}",
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(foodInfo.items) { foodItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = foodItem.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Price: KES ${foodItem.price}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                Button(
                                    onClick = { /* TODO: Add to cart logic here */ },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Add to Cart")
                                }
                            }
                        }
                    }
                }

            }



            uiState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    kotlinx.coroutines.delay(3000)
                    farmViewModel.clearErrorMessage()
                }
                Text(
                    text = error, color = Color.Red, modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
