package ru.ystu.mealmaster.domain.repository

import ru.ystu.mealmaster.domain.dto.*
import java.util.*

interface RecipeRepository {
    fun getRecipes(callback: (Result<List<RecipeDTO>>?) -> Unit)
    fun getUncheckedRecipes(callback: (Result<List<RecipeDTO>>?) -> Unit)
    fun getRecipeById(id: UUID, callback: (Result<RecipeDTO>?) -> Unit)
    fun deleteRecipeById(id: UUID, callback: (Result<Boolean>?) -> Unit)
    fun getTop10Recipes(callback: (Result<List<RecipeDTO>>?) -> Unit)
    fun getCategories(callback: (Result<List<CategoryDTO>>?) -> Unit)
    fun getRecipesByCategory(category: String, callback: (Result<List<RecipeDTO>>?) -> Unit)
    fun getRecipesByUser(username: String, approvedOnly: Boolean, callback: (Result<List<RecipeDTO>>?) -> Unit)
    fun login(username : String, password : String, callback: (Result<List<RecipeDTO>>, List<String>?) -> Unit)
    fun logout(callback: (Result<String>?) -> Unit)
    fun register(registrationRequestDTO: RegistrationRequestDTO, callback: (Result<UserDTO>) -> Unit)
    fun getAccountInfo(callback: (Result<UserDTO>) -> Unit)
    fun addRecipe(recipe: RecipeData, callback: (Result<RecipeDTO>?) -> Unit)
    fun updateRecipe(recipeId: UUID, recipe: RecipeData, callback: (Result<RecipeDTO>?) -> Unit)
    fun getCurrentUserRole(callback: (Result<String>) -> Unit)
    fun getReviewsById(id: UUID, callback: (Result<List<ReviewDTO>>) -> Unit)
    fun logViewToRecipeById(id: UUID, callback: (Result<RecipeDTO>) -> Unit)
    fun approveCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit)
    fun rejectCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit)
    fun approveUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean, callback: (Result<Boolean>?) -> Unit)
    fun rejectUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean, callback: (Result<Boolean>?) -> Unit)
}