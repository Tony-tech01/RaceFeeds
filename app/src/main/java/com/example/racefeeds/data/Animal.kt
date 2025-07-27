package com.example.racefeeds.data

data class Animal(
    val name: String,
    val image: Int,
    val breeds: List<Breed> = emptyList(),
    val generalFoodInfo: List<String> = emptyList(),
    val food: List<FoodItem> = emptyList()
)

data class Breed(
    val name: String,
    val food: List<FoodItem>
        )

data class FoodInfo(
    val title: String,
    val items: List<FoodItem>,
)

data class FoodItem(
    val name: String,
    val price: Double

)