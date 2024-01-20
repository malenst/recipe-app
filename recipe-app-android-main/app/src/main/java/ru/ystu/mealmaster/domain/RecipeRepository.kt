package ru.ystu.mealmaster.domain

import java.util.*

interface RecipeRepository {
    fun getRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getUncheckedRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getRecipeById(id: UUID, callback: (Result<Recipe>?) -> Unit)
    fun getTop10Recipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getCategories(callback: (Result<List<Category>>?) -> Unit)
    fun getRecipesByCategory(category: String, callback: (Result<List<Recipe>>?) -> Unit)
    fun login(username : String, password : String, callback: (Result<List<Recipe>>, List<String>?) -> Unit)
    fun logout(callback: (Result<String>?) -> Unit)
    fun register(registrationRequestDTO: RegistrationRequestDTO, callback: (Result<User>) -> Unit)
    fun getAccountInfo(callback: (Result<User>) -> Unit)
    fun addRecipe(recipe: RecipeData, callback: (Result<Recipe>?) -> Unit)
    fun getCurrentUserRole(callback: (Result<String>) -> Unit)
    fun logViewToRecipeById(id: UUID, callback: (Result<Recipe>) -> Unit)
}