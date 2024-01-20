package ru.ystu.mealmaster.domain

data class NutritionalInfo (
    val amount: Int,
    val measureUnit: String,
    val calories: Double,
    val carbohydrates: Double,
    val fat: Double,
    val protein: Double
)