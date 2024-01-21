package ru.ystu.mealmaster.data.repository

import ru.ystu.mealmaster.data.database.FavouriteRecipeDao
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.domain.repository.FavouriteRecipeRepository
import java.util.*


class FavouriteRecipeRepositoryImpl(private val dao: FavouriteRecipeDao) :
    FavouriteRecipeRepository {
    override fun getAllFavouriteRecipes(belongsTo: String): List<FavouriteRecipe> {
        return dao.getAllFavouriteRecipes(belongsTo)
    }

    override fun getFavouriteRecipeById(id: UUID, belongsTo: String): FavouriteRecipe? {
        return dao.getFavouriteRecipeById(id, belongsTo)
    }

    override fun addFavouriteRecipe(favouriteRecipe: FavouriteRecipe) {
        return dao.insertFavouriteRecipe(favouriteRecipe)
    }

    override fun deleteFavouriteRecipeById(id: UUID, belongsTo: String) {
        return dao.removeFavouriteRecipeById(id, belongsTo)
    }
}
