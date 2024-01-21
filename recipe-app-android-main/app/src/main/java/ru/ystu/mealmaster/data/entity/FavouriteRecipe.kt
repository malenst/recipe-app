package ru.ystu.mealmaster.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ru.ystu.mealmaster.domain.NutritionalInfo
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.Review
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
    val reviews: List<Review>?,
    val views: Int,
    @SerializedName("nutritional_info")
    @Embedded
    val nutritionalInfo: NutritionalInfo,
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
        fun fromRecipe(recipe: Recipe, belongsTo: String): FavouriteRecipe {
            return FavouriteRecipe(
                UUID.randomUUID(),
                recipe.name,
                recipe.author,
                recipe.description,
                recipe.category,
                recipe.ingredients,
                recipe.steps,
                recipe.reviews,
                recipe.views,
                recipe.nutritionalInfo,
                recipe.cookingTime,
                recipe.image,
                recipe.lastUpdated,
                recipe.id,
                belongsTo
            )
        }
    }

    override fun toString(): String {
        return "FavouriteRecipe(id=$id, name='$name', author='$author', description='$description', category='$category', ingredients=$ingredients, steps=$steps, reviews=$reviews, views=$views, nutritionalInfo=$nutritionalInfo, cookingTime='$cookingTime', image=$image, lastUpdated='$lastUpdated', belongsTo='$belongsTo')"
    }
}
