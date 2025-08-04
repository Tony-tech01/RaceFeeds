package com.example.racefeeds.ui.screens.Farm

import androidx.lifecycle.ViewModel
import com.example.racefeeds.R
import com.example.racefeeds.data.FarmSupplyData.farmTools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class FarmSupplyViewModel : ViewModel() {

    private val allTools = farmTools

    private val _uiState = MutableStateFlow(
        FarmSupplyUiState(
        categories = listOf(
            ToolCategory("Tools", R.drawable.hoe),
            ToolCategory("Feeds", R.drawable.salt),
            ToolCategory("Shelters", R.drawable.dog_shelter)
        ), selectedCategory = "Tools", tools = allTools.filter { it.category == "Tools" }))

    val uiState: StateFlow<FarmSupplyUiState> = _uiState

    fun onCategorySelected(categoryName: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = categoryName,
            tools = allTools.filter { it.category == categoryName })
    }

    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query, tools = allTools.filter {
                it.category == _uiState.value.selectedCategory && it.name.contains(
                    query,
                    ignoreCase = true
                )
            })
    }
}

