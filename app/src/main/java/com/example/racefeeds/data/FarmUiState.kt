package com.example.racefeeds.data

data class FarmUiState(
    val searchQuery: String = "",
    val displayedAnimals: List<Animal> = emptyList(),
    val selectedAnimal: Animal? = null,
    val selectedBreed: Breed? = null,
    val foodInfo: FoodInfo? = null,
    val isBreedSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingAnimals: Boolean = false,
    val isLoadingBreeds: Boolean = false,
    val errorMessage: String? = null
)
