package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.*
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getUncheckedRecipes(): List<Recipe>
    suspend fun getRecipeById(id: UUID): Recipe
    suspend fun getTop10Recipes(): List<Recipe>
    suspend fun getCategories(): List<Category>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
    suspend fun login(username: String, password: String): Pair<List<Recipe>, List<String>?>
    suspend fun register(registrationRequestDTO: RegistrationRequestDTO): User
    suspend fun addRecipe(recipe: RecipeData): Recipe
    suspend fun getCurrentUserRole(): String
    suspend fun logViewToRecipeById(id: UUID): Recipe
}