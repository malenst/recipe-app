package com.ystu.mealmaster.domain.interactor

import com.ystu.mealmaster.domain.Category
import com.ystu.mealmaster.domain.Recipe
import java.util.*

interface RecipeInteractor {
    suspend fun getRecipes(): List<Recipe>
    suspend fun getRecipeById(id: UUID): Recipe
    suspend fun getTop10Recipes(): List<Recipe>
    suspend fun getCategories(): List<Category>
    suspend fun getRecipesByCategory(category: String): List<Recipe>
}