package com.example.racefeeds.ui.screens.Animal

data class AnimalUiState(
    val searchQuery: String = "",
    val displayedAnimals: List<Animal> = emptyList(),
    val selectedAnimal: Animal? = null,
    val selectedBreed: Breed? = null,
    val foodInfo: FoodInfo? = null,
    val isBreedSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingAnimals: Boolean = false,
    val isLoadingBreeds: Boolean = false,
    val errorMessage: String? = null,
    val matchingFoodItems: List<FoodItem> = emptyList(),
    val matchingBreeds: List<Breed> = emptyList(),
    val matchingFeedsWithContext: List<FeedContext> = emptyList()
)