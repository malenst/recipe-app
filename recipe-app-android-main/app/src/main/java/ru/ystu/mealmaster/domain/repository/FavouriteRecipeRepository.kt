package ru.ystu.mealmaster.domain.repository

import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import java.util.*

interface FavouriteRecipeRepository {
    fun getAllFavouriteRecipes(belongsTo: String): List<FavouriteRecipe>
    fun getFavouriteRecipeById(id: UUID, belongsTo: String): FavouriteRecipe?
    fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe)
    fun deleteFavouriteRecipeById(id: UUID, belongsTo: String)
}