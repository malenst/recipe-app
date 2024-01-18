package ru.ystu.mealmaster.domain

data class NutritionalInfo (
    val amount: Int,
    val unit: String,
    val calories: Int,
    val carbohydrates: Double,
    val fat: Double,
    val protein: Double
)