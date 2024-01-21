package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import java.util.*

interface FavouriteRecipeInteractor {
    suspend fun getAllFavouriteRecipes(belongsTo: String): List<FavouriteRecipe>
    suspend fun getFavouriteRecipeById(id: UUID, belongsTo: String): FavouriteRecipe?
    suspend fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe)
    suspend fun deleteFavouriteRecipeById(id: UUID, belongsTo: String)
}