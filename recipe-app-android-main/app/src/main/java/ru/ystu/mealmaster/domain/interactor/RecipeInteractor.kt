package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getUncheckedRecipes(): List<Recipe>
    suspend fun getRecipeById(id: UUID): Recipe
    suspend fun getTop10Recipes(): List<Recipe>
    suspend fun getCategories(): List<Category>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
}