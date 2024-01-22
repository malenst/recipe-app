package ru.ystu.mealmaster.domain.repository

import ru.ystu.mealmaster.domain.*
import java.util.*

interface RecipeRepository {
    fun getRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getUncheckedRecipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getRecipeById(id: UUID, callback: (Result<Recipe>?) -> Unit)
    fun deleteRecipeById(id: UUID, callback: (Result<Boolean>?) -> Unit)
    fun getTop10Recipes(callback: (Result<List<Recipe>>?) -> Unit)
    fun getCategories(callback: (Result<List<Category>>?) -> Unit)
    fun getRecipesByCategory(category: String, callback: (Result<List<Recipe>>?) -> Unit)
    fun getRecipesByUser(username: String, approvedOnly: Boolean, callback: (Result<List<Recipe>>?) -> Unit)
    fun login(username : String, password : String, callback: (Result<List<Recipe>>, List<String>?) -> Unit)
    fun logout(callback: (Result<String>?) -> Unit)
    fun register(registrationRequestDTO: RegistrationRequestDTO, callback: (Result<User>) -> Unit)
    fun getAccountInfo(callback: (Result<User>) -> Unit)
    fun addRecipe(recipe: RecipeData, callback: (Result<Recipe>?) -> Unit)
    fun getCurrentUserRole(callback: (Result<String>) -> Unit)
    fun getReviewsById(id: UUID, callback: (Result<List<Review>>) -> Unit)
    fun logViewToRecipeById(id: UUID, callback: (Result<Recipe>) -> Unit)
    fun approveCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit)
    fun rejectCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit)
    fun approveUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean, callback: (Result<Boolean>?) -> Unit)
    fun rejectUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean, callback: (Result<Boolean>?) -> Unit)
}