package com.example.racefeeds.ui.screens.Farm

import com.example.racefeeds.ui.screens.Farm.ToolCategory

data class FarmSupplyUiState(
    val selectedCategory: String = "Tools",
    val categories: List<ToolCategory> = emptyList(),
    val tools: List<FarmTool> = emptyList(),
    val searchQuery: String = ""
)