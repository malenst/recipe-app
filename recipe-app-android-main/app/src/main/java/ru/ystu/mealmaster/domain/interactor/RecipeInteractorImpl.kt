package ru.ystu.mealmaster.domain.interactor

import kotlinx.coroutines.suspendCancellableCoroutine
import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.RecipeData
import ru.ystu.mealmaster.domain.RecipeRepository
import ru.ystu.mealmaster.domain.RegistrationRequestDTO
import ru.ystu.mealmaster.domain.Review
import ru.ystu.mealmaster.domain.User
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

    override suspend fun login(username: String, password: String): Pair<List<Recipe>, List<String>?> = suspendCoroutine { continuation ->
        repository.login(username, password) { result, cookies ->
            if (result.isSuccess) {
                continuation.resume(Pair(result.getOrNull() ?: emptyList(), cookies))
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to login"))
            }
        }
    }

    override suspend fun logout(): String = suspendCoroutine {continuation ->
        repository.logout { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to logout"))
            }
        }
    }

    override suspend fun register(registrationRequestDTO: RegistrationRequestDTO): User = suspendCoroutine { continuation ->
        repository.register(registrationRequestDTO) { result ->
            if (result.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to register"))
            }
        }
    }

    override suspend fun getAccountInfo(): User = suspendCoroutine {continuation ->
        repository.getAccountInfo() { result ->
            if (result.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to register"))
            }
        }
    }

    override suspend fun addRecipe(recipe: RecipeData): Recipe = suspendCoroutine { continuation ->
        repository.addRecipe(recipe) { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Error adding recipe"))
            }
        }
    }

    override suspend fun getCurrentUserRole(): String = suspendCoroutine { continuation ->
        repository.getCurrentUserRole() { result ->
            if (result.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load current user role"))
            }
        }
    }

    override suspend fun getReviewsById(id: UUID): List<Review> = suspendCoroutine { continuation ->
        repository.getReviewsById(id) { result ->
            if (result!!.isSuccess) {
                continuation.resume(result.getOrNull() ?: emptyList())
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to load recipes for id: $id"))
            }
        }
    }

    override suspend fun logViewToRecipeById(id: UUID): Recipe = suspendCoroutine { continuation ->
        repository.logViewToRecipeById(id) { result ->
            if (result.isSuccess) {
                continuation.resume(result.getOrNull()!!)
            } else {
                continuation.resumeWithException(result.exceptionOrNull() ?: RuntimeException("Failed to log view to recipe"))
            }
        }
    }
}