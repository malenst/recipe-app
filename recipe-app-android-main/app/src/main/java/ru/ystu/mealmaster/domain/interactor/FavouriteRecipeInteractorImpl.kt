package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.domain.repository.FavouriteRecipeRepository
import java.util.*

class FavouriteRecipeInteractorImpl(private val favouriteRecipeRepository: FavouriteRecipeRepository): FavouriteRecipeInteractor {
    override suspend fun getAllFavouriteRecipes(belongsTo: String): List<FavouriteRecipe> {
        return favouriteRecipeRepository.getAllFavouriteRecipes(belongsTo)
    }

    override suspend fun getFavouriteRecipeById(id: UUID, belongsTo: String): FavouriteRecipe? {
        return favouriteRecipeRepository.getFavouriteRecipeById(id, belongsTo)
    }

    override suspend fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe) {
        return favouriteRecipeRepository.addFavouriteRecipe(favouriteRecipe)
    }

    override suspend fun deleteFavouriteRecipeById(id: UUID, belongsTo: String) {
        return favouriteRecipeRepository.deleteFavouriteRecipeById(id, belongsTo)
    }
}