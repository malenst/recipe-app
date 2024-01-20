package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.RecipeData
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getUncheckedRecipes(): List<Recipe>
    suspend fun getRecipeById(id: UUID): Recipe
    suspend fun getTop10Recipes(): List<Recipe>
    suspend fun getCategories(): List<Category>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
    suspend fun login(username: String, password: String): Pair<List<Recipe>, List<String>?>
    suspend fun addRecipe(recipe: RecipeData): Recipe
    suspend fun getCurrentUserRole(): String
    suspend fun logViewToRecipeById(id: UUID): Recipe
}