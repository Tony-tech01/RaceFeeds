package com.example.racefeeds.ui.screens.Farm

import FarmTool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racefeeds.R
import com.example.racefeeds.data.FarmSupplyData.farmTools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ToolUiState {
    object Loading : ToolUiState()
    data class Success(val tool: FarmTool) : ToolUiState()
    data class Error(val message: String) : ToolUiState()
}

class FarmSupplyViewModel : ViewModel() {

    private val allTools = farmTools

    private val _uiState = MutableStateFlow(
        FarmSupplyUiState(
            categories = listOf(
                ToolCategory("Tools", R.drawable.hoe),
                ToolCategory("Feeds", R.drawable.salt),
                ToolCategory("Shelters", R.drawable.dog_shelter)
            ),
            selectedCategory = "Tools",
            tools = allTools.filter { it.category == "Tools" }
        )
    )
    val uiState: StateFlow<FarmSupplyUiState> = _uiState

    fun onCategorySelected(categoryName: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = categoryName,
            tools = allTools.filter { it.category == categoryName }
        )
    }

    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            tools = if (query.isNotBlank()) {
                allTools.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            } else {
                allTools.filter {
                    it.category == _uiState.value.selectedCategory
                }
            }
        )
    }

    private val _toolDetailState = MutableStateFlow<ToolUiState>(ToolUiState.Loading)
    val toolDetailState: StateFlow<ToolUiState> = _toolDetailState

    fun loadToolById(toolId: String) {
        viewModelScope.launch {
            _toolDetailState.value = ToolUiState.Loading
            val tool = allTools.find { it.id == toolId }
            _toolDetailState.value = when {
                tool != null -> ToolUiState.Success(tool)
                else -> ToolUiState.Error("Tool not found.")
            }
        }
    }
}