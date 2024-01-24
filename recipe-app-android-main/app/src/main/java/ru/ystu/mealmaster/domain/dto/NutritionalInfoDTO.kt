package ru.ystu.mealmaster.domain.dto

data class NutritionalInfoDTO (
    val amount: Int?,
    val measureUnit: String?,
    val calories: Double?,
    val carbohydrates: Double?,
    val fat: Double?,
    val protein: Double?
)