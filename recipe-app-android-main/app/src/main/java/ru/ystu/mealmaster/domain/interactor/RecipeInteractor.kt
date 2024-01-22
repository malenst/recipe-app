package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.*
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getUncheckedRecipes(): List<Recipe>
    suspend fun getRecipeById(id: UUID): Recipe
    suspend fun deleteRecipeById(id: UUID): Boolean
    suspend fun getTop10Recipes(): List<Recipe>
    suspend fun getCategories(): List<Category>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
    suspend fun getRecipesByUser(username: String, approvedOnly: Boolean): List<Recipe>
    suspend fun login(username: String, password: String): Pair<List<Recipe>, List<String>?>
    suspend fun logout(): String
    suspend fun register(registrationRequestDTO: RegistrationRequestDTO): User
    suspend fun getAccountInfo(): User
    suspend fun addRecipe(recipe: RecipeData): Recipe
    suspend fun updateRecipe(recipeId: UUID, recipe: RecipeData): Recipe
    suspend fun getCurrentUserRole(): String
    suspend fun getReviewsById(id: UUID): List<Review>
    suspend fun logViewToRecipeById(id: UUID): Recipe
    suspend fun approveCreateRecipe(recipeId: UUID): Boolean
    suspend fun rejectCreateRecipe(recipeId: UUID): Boolean
    suspend fun approveUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean): Boolean
    suspend fun rejectUpdateOrDeleteRecipe(recipeId: UUID, isDraft: Boolean): Boolean
}