package com.tiodev.vegtummy.domain

import java.util.UUID

interface RecipeRepository {
    fun getRecipes(callback: (List<Recipe>?) -> Unit)
    fun getRecipeById(id: UUID, callback: (Recipe?) -> Unit)
    fun getTop10Recipes(callback: (List<Recipe>?) -> Unit)
    fun getCategories(callback: (List<Category>?) -> Unit)
    fun getRecipesByCategory(category: String, callback: (List<Recipe>?) -> Unit)
}