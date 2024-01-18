package ru.ystu.mealmaster.domain

data class RecipeData(
    val name: String,
    val description: String,
    val category: String,
    val ingredients: Map<String, String>,
    val steps: Map<String, String>,
    val nutritionalInfo: NutritionalInfo,
    val cookingTime: Int/*,
    val image: String*/
)
