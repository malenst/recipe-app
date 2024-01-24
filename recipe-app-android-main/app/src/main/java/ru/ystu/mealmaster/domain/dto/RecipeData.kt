package ru.ystu.mealmaster.domain.dto

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class RecipeData(
    val name: String?,
    val description: String?,
    val category: String?,
    val ingredients: Map<String, String>?,
    val steps: Map<String, String>?,
    @SerializedName("nutritional_info")
    @Embedded
    val nutritionalInfoDTO: NutritionalInfoDTO?,
    @SerializedName("cooking_time")
    val cookingTime: Int?,
    val image: String?
)
