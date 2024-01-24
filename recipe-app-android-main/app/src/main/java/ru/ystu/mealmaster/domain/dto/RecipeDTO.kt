package ru.ystu.mealmaster.domain.dto

import com.google.gson.annotations.SerializedName
import ru.ystu.mealmaster.domain.enumeration.ChangeType
import java.util.*

data class RecipeDTO(
    val id: UUID,
    val name: String,
    val author: String,
    val description: String,
    val category: String,
    val ingredients: Map<String, String>,
    val steps: Map<String, String>,
    val reviewDTOS: List<ReviewDTO>?,
    val views: Int,
    @SerializedName("nutritional_info")
    val nutritionalInfoDTO: NutritionalInfoDTO,
    @SerializedName("cooking_time")
    val cookingTime: String,
    @SerializedName("image_url")
    val image: String?,
    @SerializedName("last_updated")
    val lastUpdated: String,

    // RecipeDraft fields
    @SerializedName("change_type")
    val changeType: ChangeType?
)
