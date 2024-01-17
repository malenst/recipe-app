package ru.ystu.mealmaster.domain

import java.util.*

interface RecipeRepository {
    fun getRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getUncheckedRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getRecipeById(id: UUID, callback: (Result<Recipe>?) -> Unit)
    fun getTop10Recipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getCategories(callback: (Result<List<Category>>?) -> Unit)
    fun getRecipesByCategory(category: String, callback: (Result<List<Recipe>>?) -> Unit)
    fun getCurrentUserRole(callback: (Result<String>) -> Unit)
}