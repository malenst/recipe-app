package ru.ystu.mealmaster.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ru.ystu.mealmaster.domain.dto.NutritionalInfoDTO
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.domain.dto.ReviewDTO
import java.io.Serializable
import java.util.*

@Entity(tableName = "favourite_recipe")
data class FavouriteRecipe(
    @PrimaryKey
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
    @Embedded
    val nutritionalInfoDTO: NutritionalInfoDTO,
    @SerializedName("cooking_time")
    val cookingTime: String,
    @SerializedName("image_url")
    val image: String?,
    @SerializedName("last_updated")
    val lastUpdated: String,
    val apiId: UUID,
    val belongsTo: String
) : Serializable {
    companion object {
        fun fromRecipe(recipeDTO: RecipeDTO, belongsTo: String): FavouriteRecipe {
            return FavouriteRecipe(
                UUID.randomUUID(),
                recipeDTO.name,
                recipeDTO.author,
                recipeDTO.description,
                recipeDTO.category,
                recipeDTO.ingredients,
                recipeDTO.steps,
                recipeDTO.reviewDTOS,
                recipeDTO.views,
                recipeDTO.nutritionalInfoDTO,
                recipeDTO.cookingTime,
                recipeDTO.image,
                recipeDTO.lastUpdated,
                recipeDTO.id,
                belongsTo
            )
        }
    }

    override fun toString(): String {
        return "FavouriteRecipe(id=$id, name='$name', author='$author', description='$description', category='$category', ingredients=$ingredients, steps=$steps, reviews=$reviewDTOS, views=$views, nutritionalInfo=$nutritionalInfoDTO, cookingTime='$cookingTime', image=$image, lastUpdated='$lastUpdated', belongsTo='$belongsTo')"
    }
}
