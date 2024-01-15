package com.ystu.mealmaster.domain

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Recipe(
    val id: UUID,
    val name: String,
    val description: String,
    val category: String,
    val ingredients: Map<String, String>,
    val steps: Map<String, String>,
    val reviews: List<Review>?,
    val views: Int,
    @SerializedName("nutritional_info")
    val nutritionalInfo: String,
    @SerializedName("cooking_time")
    val cookingTime: String,
    @SerializedName("image_url")
    val image: String?,
    @SerializedName("last_updated")
    val lastUpdated: String
)
