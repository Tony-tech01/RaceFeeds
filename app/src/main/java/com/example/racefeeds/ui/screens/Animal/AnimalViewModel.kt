package com.example.racefeeds.ui.screens.Animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalUiState())
    val uiState: StateFlow<AnimalUiState> = _uiState.asStateFlow()

    init {
        loadInitialAnimals()
    }

    private fun loadInitialAnimals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingAnimals = true) }
            try {
                val initialAnimals = AnimalData.animals
                _uiState.update {
                    it.copy(
                        displayedAnimals = initialAnimals, isLoadingAnimals = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Failed to load initial animals", isLoadingAnimals = false
                    )
                }
            }
        }
    }


    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterAnimals(query)
    }

    private fun filterAnimals(query: String) {
        viewModelScope.launch {
            val loweredQuery = query.trim().lowercase()

            val matchingFeedsWithContext = mutableListOf<FeedContext>()
            val matchingAnimals = mutableSetOf<Animal>()
            val matchingBreeds = mutableSetOf<Breed>()

            AnimalData.animals.forEach { animal ->
                val isAnimalMatch = animal.name.lowercase().contains(loweredQuery)

                if (isAnimalMatch) {
                    matchingAnimals += animal
                    val filteredBreeds = filterBreedsForAnimal(animal, loweredQuery)
                    matchingBreeds += filteredBreeds
                }

                animal.food.forEach { food ->
                    if (food.name.lowercase().contains(loweredQuery)) {
                        matchingFeedsWithContext += FeedContext(feedItem = food, breed = null, animal = animal)
                        matchingAnimals += animal
                    }
                }

                animal.breeds.forEach { breed ->
                    val isBreedMatch = breed.name.lowercase().contains(loweredQuery)
                    if (isBreedMatch) {
                        matchingBreeds += breed
                        matchingAnimals += animal
                    }

                    breed.food.forEach { food ->
                        if (food.name.lowercase().contains(loweredQuery)) {
                            matchingFeedsWithContext += FeedContext(feedItem = food, breed = breed, animal = animal)
                            matchingBreeds += breed
                            matchingAnimals += animal
                        }
                    }
                }
            }

            _uiState.update {
                it.copy(
                    searchQuery = query,
                    displayedAnimals = matchingAnimals.toList(),
                    matchingBreeds = matchingBreeds.toList(),
                    matchingFeedsWithContext = matchingFeedsWithContext
                )
            }
        }
    }

    fun onFeedSelected(feedItem: FoodItem, breed: Breed?, animal: Animal) {
        val title = when {
            breed != null -> "Feed Info for ${breed.name} (${animal.name})"
            else -> "Feed Info for ${animal.name}"
        }

        _uiState.update {
            it.copy(
                searchQuery = "",
                matchingFeedsWithContext = emptyList(),
                displayedAnimals = listOf(animal),
                selectedAnimal = animal,
                selectedBreed = breed,
                foodInfo = FoodInfo(title = title, items = listOf(feedItem)),
                errorMessage = null,
                isBreedSheetVisible = breed == null && animal.breeds.isNotEmpty()
            )
        }
    }

    fun filterBreedsForAnimal(animal: Animal, feedQuery: String): List<Breed> {
        return animal.breeds.filter { breed ->
            breed.food.any { feed ->
                feed.name.contains(feedQuery, ignoreCase = true)
            }
        }
    }

    fun onAnimalSelected(animal: Animal) {
        if (animal.breeds.isEmpty()) {

            _uiState.update {
                it.copy(
                    selectedAnimal = animal,
                    selectedBreed = null,
                    foodInfo = FoodInfo(title = "Feeds for ${animal.name}", items = animal.food),
                    errorMessage = null,
                    isBreedSheetVisible = false
                )
            }
        } else {

            _uiState.update {
                it.copy(
                    selectedAnimal = animal,
                    selectedBreed = null,
                    foodInfo = null,
                    errorMessage = null,
                    isBreedSheetVisible = true
                )
            }
        }
    }

    fun onBreedSelected(breed: Breed) {
        try {
            val food = AnimalData.getFoodForBreed(breed.name)
            if (food != null) {
                _uiState.update {
                    it.copy(selectedBreed = breed, foodInfo = food, errorMessage = null)
                }
            } else {
                _uiState.update {
                    it.copy(foodInfo = null, errorMessage = "No food info found for ${breed.name}")
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(foodInfo = null, errorMessage = "Failed to load food info")
            }
        }
    }


    fun closeBreedSheet() {
        _uiState.update {
            it.copy(
                isBreedSheetVisible = false,
                selectedAnimal = null,
                selectedBreed = null,
                foodInfo = null,
                errorMessage = null
            )
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}