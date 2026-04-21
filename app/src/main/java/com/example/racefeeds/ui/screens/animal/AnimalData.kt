package com.example.racefeeds.ui.screens.animal

import com.example.racefeeds.R

object AnimalData {
    val animals = listOf(
        Animal(
            name = "Cow",
            image = R.drawable.cow,
            breeds = listOf(
                Breed(
                    name = "Holstein",
                    food = listOf(
                        FoodItem(name = "Silage", price = 300.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Hay", price = 250.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Dairy Meal", price = 400.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Grass", price = 100.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Mineral Block", price = 150.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Mixed Feed", price = 220.0, imageRes = R.drawable.broiler, description = ""),

                        )
                ),
                Breed(
                    name = "Jersey",
                    food = listOf(
                        FoodItem(name = "Napier Grass", price = 200.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Lucerne", price = 280.0, imageRes = R.drawable.broiler, description = ""),

                        )
                )
            )
        ),
        Animal(
            name = "Goat",
            image = R.drawable.goat,
            breeds = listOf(
                Breed(
                    name = "Boer",
                    food = listOf(
                        FoodItem(name = "Grass", price = 100.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Mineral Block", price = 150.0, imageRes = R.drawable.broiler, description = "")
                    )
                ),
                Breed(
                    name = "Saanen",
                    food = listOf(
                        FoodItem(name = "Lucerne", price = 180.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Mixed Feed", price = 220.0, imageRes = R.drawable.broiler, description = "")
                    )
                )
            )
        ),
        Animal(
            name = "Chicken",
            image = R.drawable.chicken,
            breeds = listOf(
                Breed(
                    name = "Broiler",
                    food = listOf(
                        FoodItem(name = "Broiler Starter", price = 600.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Growers Mash", price = 580.0, imageRes = R.drawable.broiler, description = "")
                    )
                ),
                Breed(
                    name = "Layer",
                    food = listOf(
                        FoodItem(name = "Layers Mash", price = 560.0, imageRes = R.drawable.broiler, description = ""),
                        FoodItem(name = "Maize Germ", price = 200.0, imageRes = R.drawable.broiler, description = "")
                    )
                )
            )
        ),
        Animal(
            name = "Pig",
            image = R.drawable.pig,
            food = listOf(
                FoodItem(name = "Silage", price = 300.0, imageRes = R.drawable.broiler, description = ""),
                FoodItem(name = "Hay", price = 250.0, imageRes = R.drawable.broiler, description = ""),
                FoodItem(name = "Dairy Meal", price = 400.0, imageRes = R.drawable.broiler, description = "")
            )
        )

    )
    fun getFoodForBreed(breedName: String): FoodInfo? {
        animals.forEach { animal ->
            animal.breeds.forEach { breed ->
                if (breed.name == breedName) {
                    return FoodInfo(
                        title = "Food for ${animal.name} - ${breed.name}",
                        items = breed.food
                    )
        }
            }
    }
        return null
}
}