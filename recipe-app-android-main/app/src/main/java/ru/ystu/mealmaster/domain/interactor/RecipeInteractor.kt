package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.dto.*
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<RecipeDTO>
    suspend fun getUncheckedRecipes(): List<RecipeDTO>
    suspend fun getRecipeById(id: UUID): RecipeDTO
    suspend fun deleteRecipeById(id: UUID): Boolean
    suspend fun getTop10Recipes(): List<RecipeDTO>
    suspend fun getCategories(): List<CategoryDTO>
    suspend fun getRecipesByCategory(category: String): List<RecipeDTO>
    suspend fun getRecipesByUser(username: String, approvedOnly: Boolean): List<RecipeDTO>
    suspend fun login(username: String, password: String): Pair<List<RecipeDTO>, List<String>?>
    suspend fun logout(): String
    suspend fun register(registrationRequestDTO: RegistrationRequestDTO): UserDTO
    suspend fun getAccountInfo(): UserDTO
    suspend fun addRecipe(recipe: RecipeData): RecipeDTO
    suspend fun updateRecipe(recipeId: UUID, recipe: RecipeData): RecipeDTO
    suspend fun getCurrentUserRole(): String
    suspend fun getReviewsById(id: UUID): List<ReviewDTO>
    suspend fun logViewToRecipeById(id: UUID): RecipeDTO
    suspend fun approveCreateRecipe(recipeId: UUID): Boolean
    suspend fun rejectCreateRecipe(recipeId: UUID): Boolean
    suspend fun approveUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean): Boolean
    suspend fun rejectUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean): Boolean
}