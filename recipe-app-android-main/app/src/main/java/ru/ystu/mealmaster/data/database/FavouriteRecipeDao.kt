package ru.ystu.mealmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import java.util.*

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM favourite_recipe WHERE belongsTo = :belongsTo")
    fun getAllFavouriteRecipes(belongsTo: String): List<FavouriteRecipe>

    @Query("SELECT * FROM favourite_recipe WHERE apiId = :apiRecipeId AND belongsTo = :belongsTo")
    fun getFavouriteRecipeById(apiRecipeId: UUID, belongsTo: String): FavouriteRecipe?

    @Insert
    fun insertFavouriteRecipe(favouriteRecipe: FavouriteRecipe)

    @Query("DELETE FROM favourite_recipe WHERE apiId = :apiRecipeId AND belongsTo = :belongsTo")
    fun removeFavouriteRecipeById(apiRecipeId: UUID, belongsTo: String)

    @Query("DELETE FROM favourite_recipe")
    fun clearFavouriteRecipes()
}