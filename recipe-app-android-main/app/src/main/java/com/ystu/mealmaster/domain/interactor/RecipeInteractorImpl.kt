package com.ystu.mealmaster.domain.interactor

import com.ystu.mealmaster.domain.Category
import com.ystu.mealmaster.domain.Recipe
import com.ystu.mealmaster.domain.RecipeRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeInteractorImpl(private val repository: RecipeRepository) : RecipeInteractor {
    override suspend fun getRecipes(): List<Recipe> = suspendCancellableCoroutine { continuation ->
        repository.getRecipes { recipes ->
            if (recipes != null) {
                continuation.resume(recipes)
            } else {
                continuation.resumeWithException(RuntimeException("Failed to load recipes"))
            }
        }
    }

    override suspend fun getRecipeById(id: UUID): Recipe = suspendCoroutine { continuation ->
        repository.getRecipeById(id) { recipe ->
            if (recipe != null) {
                continuation.resume(recipe)
            } else {
                continuation.resumeWithException(RuntimeException("Recipe not found"))
            }
        }
    }

    override suspend fun getTop10Recipes(): List<Recipe> = suspendCoroutine { continuation ->
        repository.getTop10Recipes { recipes ->
            if (recipes != null) {
                continuation.resume(recipes)
            } else {
                continuation.resumeWithException(RuntimeException("Failed to load top 10 recipes"))
            }
        }
    }

    override suspend fun getCategories(): List<Category> = suspendCoroutine { continuation ->
        repository.getCategories { categories ->
            if (categories != null) {
                continuation.resume(categories)
            } else {
                continuation.resumeWithException(RuntimeException("Failed to load categories"))
            }
        }
    }

    override suspend fun getRecipesByCategory(category: String): List<Recipe> = suspendCoroutine { continuation ->
        repository.getRecipesByCategory(category) { recipes ->
            if (recipes != null) {
                continuation.resume(recipes)
            } else {
                continuation.resumeWithException(RuntimeException("Failed to load recipes for category: $category"))
            }
        }
    }
}