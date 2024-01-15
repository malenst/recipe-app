package ru.ystu.mealmaster.domain.interactor

import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.RecipeRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeInteractorImpl(private val repository: RecipeRepository) : RecipeInteractor {
    override suspend fun getRecipes(): List<Recipe> = suspendCancellableCoroutine { continuation ->
        repository.getRecipes { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load recipes"))
            }
        }
    }

    override suspend fun getUncheckedRecipes(): List<Recipe> = suspendCancellableCoroutine { continuation ->
        repository.getUncheckedRecipes { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load recipes"))
            }
        }
    }

    override suspend fun getRecipeById(id: UUID): Recipe = suspendCoroutine { continuation ->
        repository.getRecipeById(id) { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: throw RuntimeException("Recipe not found"))
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Error fetching recipe by ID"))
            }
        }
    }

    override suspend fun getTop10Recipes(): List<Recipe> = suspendCoroutine { continuation ->
        repository.getTop10Recipes { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load top 10 recipes"))
            }
        }
    }

    override suspend fun getCategories(): List<Category> = suspendCoroutine { continuation ->
        repository.getCategories { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load categories"))
            }
        }
    }

    override suspend fun getRecipesByCategory(category: String): List<Recipe> = suspendCoroutine { continuation ->
        repository.getRecipesByCategory(category) { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load recipes for category: $category"))
            }
        }
    }

}