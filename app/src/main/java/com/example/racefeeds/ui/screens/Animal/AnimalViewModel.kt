package com.example.racefeeds.ui.screens.Animal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.ui.screens.Animal.AnimalUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalUiState())
    val uiState: StateFlow<AnimalUiState> = _uiState.asStateFlow()

    init{
        loadInitialAnimals()
    }
    private fun loadInitialAnimals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingAnimals = true) }
            try {
                val initialAnimals = AnimalData.animals
                _uiState.update {
                    it.copy(
                        displayedAnimals = initialAnimals,
                        isLoadingAnimals = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Failed to load initial animals",
                        isLoadingAnimals = false
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
            val filteredList = if (query.isBlank()) {
                AnimalData.animals
            } else {
                AnimalData.animals.filter {
                    it.name.trim().contains(query.trim(), ignoreCase = true)
                }
            }
            println("Filtered list: ${filteredList.map { it.name }}")
            _uiState.update { it.copy(displayedAnimals = filteredList) }
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