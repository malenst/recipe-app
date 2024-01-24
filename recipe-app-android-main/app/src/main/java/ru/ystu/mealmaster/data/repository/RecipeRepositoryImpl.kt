package ru.ystu.mealmaster.data.repository

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ystu.mealmaster.domain.dto.ApiResponseDto
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.domain.dto.*
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.util.persistent.CustomPersistentCookieJar
import ru.ystu.mealmaster.util.sharedpref.SharedPrefManager
import java.util.*


class RecipeRepositoryImpl(private val api: RecipeApiService, private val context: Context) : RecipeRepository {
    override fun getRecipes(callback: (Result<List<RecipeDTO>>?) -> Unit) {
        api.getRecipes().enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                val manager = SharedPrefManager(context)
                val sessionId: String = manager.getSessionId().toString()
                Log.d("SESSION", sessionId)

                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getUncheckedRecipes(callback: (Result<List<RecipeDTO>>?) -> Unit) {
        api.getUncheckedRecipes().enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
                    Log.d("APELSIN", response.body()?.response.toString())
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    Log.d("MANGO", "MANGO")
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {

                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipeById(id: UUID, callback: (Result<RecipeDTO>?) -> Unit) {
        api.getRecipeById(id).enqueue(object : Callback<ApiResponseDto<RecipeDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<RecipeDTO>>,
                response: Response<ApiResponseDto<RecipeDTO>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<RecipeDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun deleteRecipeById(id: UUID, callback: (Result<Boolean>?) -> Unit) {
        api.deleteRecipeById(id).enqueue(object : Callback<ApiResponseDto<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Boolean>>,
                response: Response<ApiResponseDto<Boolean>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Boolean>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }


    override fun getTop10Recipes(callback: (Result<List<RecipeDTO>>?) -> Unit) {
        api.getTop10Recipes().enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getCategories(callback: (Result<List<CategoryDTO>>?) -> Unit) {
        api.getCategories().enqueue(object : Callback<ApiResponseDto<List<CategoryDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<CategoryDTO>>>,
                response: Response<ApiResponseDto<List<CategoryDTO>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<CategoryDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipesByCategory(category: String, callback: (Result<List<RecipeDTO>>?) -> Unit) {
        api.getRecipesByCategory(category).enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipesByUser(username: String, approvedOnly: Boolean, callback: (Result<List<RecipeDTO>>?) -> Unit) {
        api.getRecipesByUser(username, approvedOnly).enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun login(username: String, password: String, callback: (Result<List<RecipeDTO>>, List<String>?) -> Unit) {
        api.login(username, password).enqueue(object : Callback<ApiResponseDto<List<RecipeDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<RecipeDTO>>>,
                response: Response<ApiResponseDto<List<RecipeDTO>>>
            ) {
                val headers = response.headers()
                val cookies = headers.values("Set-Cookie")
                val manager = SharedPrefManager(context)
                val jsessionIdCookie = cookies.find { it.startsWith("JSESSIONID=") }
                jsessionIdCookie?.let {
                    val jsessionIdValue = it.substringAfter("JSESSIONID=").substringBefore(";")
                    manager.saveSession(jsessionIdValue)
                }
                val loginError = headers.values("Location").any { cookie -> "/login?error" in cookie }

                if ((response.isSuccessful || response.code() == 301 || response.code() == 302) && !loginError) {
                    Log.d("SUCCREPOLOG", "a")
                    callback(Result.success(response.body()?.response ?: emptyList()), cookies)
                } else {
                    Log.d("ERRREPOLOG", "a")
                    val errorMessage = "Ошибка запроса: HTTP ${response.code()} ${response.message()}, Body: ${
                        response.errorBody()?.string()
                    }"
                    callback(Result.failure(Exception(errorMessage)), cookies)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<RecipeDTO>>>, t: Throwable) {
                callback(Result.failure(t), emptyList())
            }
        })
    }

    override fun logout(callback: (Result<String>?) -> Unit) {
        api.logout().enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                try {
                    if (response.isSuccessful || response.code() == 301 || response.code() == 302 || response.code() == 401) {
                        val manager = SharedPrefManager(context)
                        manager.clearSession()

                        (RecipeApi.client.cookieJar as? CustomPersistentCookieJar)?.clear()

                        Log.d("LOGOUT", "Logged out successfully.")
                        callback(Result.success(response.message()))
                    }
                } catch (_: Exception) {

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun register(registrationRequestDTO: RegistrationRequestDTO, callback: (Result<UserDTO>) -> Unit) {
        api.register(registrationRequestDTO).enqueue(object : Callback<ApiResponseDto<UserDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<UserDTO>>,
                response: Response<ApiResponseDto<UserDTO>>
            ) {
                if ((response.isSuccessful || response.code() == 301 || response.code() == 302)) {
                    callback(Result.success(response.body()?.response!!))
                } else {
                    val errorMessage = "Ошибка запроса: HTTP ${response.code()} ${response.message()}, Body: ${
                        response.errorBody()?.string()
                    }"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<UserDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getAccountInfo(callback: (Result<UserDTO>) -> Unit) {
        api.getAccountInfo().enqueue(object : Callback<ApiResponseDto<UserDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<UserDTO>>,
                response: Response<ApiResponseDto<UserDTO>>
            ) {
                try {
                    if ((response.isSuccessful || response.code() == 301 || response.code() == 302)) {
                        callback(Result.success(response.body()?.response!!))
                    }
                } catch (ignored: Exception) {
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<UserDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun addRecipe(recipe: RecipeData, callback: (Result<RecipeDTO>?) -> Unit) {
        api.addRecipe(recipe).enqueue(object : Callback<ApiResponseDto<RecipeDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<RecipeDTO>>,
                response: Response<ApiResponseDto<RecipeDTO>>
            ) {
                if (response.isSuccessful || response.code() == 201 || response.code() == 301 || response.code() == 302) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<RecipeDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun updateRecipe(recipeId: UUID, recipe: RecipeData, callback: (Result<RecipeDTO>?) -> Unit) {
        api.updateRecipe(recipeId, recipe).enqueue(object : Callback<ApiResponseDto<RecipeDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<RecipeDTO>>,
                response: Response<ApiResponseDto<RecipeDTO>>
            ) {
                if (response.isSuccessful || response.code() == 201 || response.code() == 301 || response.code() == 302) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<RecipeDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getCurrentUserRole(callback: (Result<String>) -> Unit) {
        api.getCurrentUserRole().enqueue(object : Callback<ApiResponseDto<String>> {
            override fun onResponse(
                call: Call<ApiResponseDto<String>>,
                response: Response<ApiResponseDto<String>>
            ) {
                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    val errorMessage = "Ошибка запроса: HTTP ${response.code()} ${response.message()}, Body: ${
                        response.errorBody()?.string()
                    }"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<String>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getReviewsById(id: UUID, callback: (Result<List<ReviewDTO>>) -> Unit) {
        api.getReviewsById(id).enqueue(object : Callback<ApiResponseDto<List<ReviewDTO>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<ReviewDTO>>>,
                response: Response<ApiResponseDto<List<ReviewDTO>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<ReviewDTO>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun logViewToRecipeById(id: UUID, callback: (Result<RecipeDTO>) -> Unit) {
        api.logViewToRecipeById(id).enqueue(object : Callback<ApiResponseDto<RecipeDTO>> {
            override fun onResponse(
                call: Call<ApiResponseDto<RecipeDTO>>,
                response: Response<ApiResponseDto<RecipeDTO>>
            ) {
                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(Result.success(responseBody.response))
                    } else {
                        callback(Result.failure(Exception("Ошибка: тело ответа отсутствует")))
                    }
                } else {
                    val errorMessage = "Ошибка запроса: HTTP ${response.code()} ${response.message()}, Body: ${
                        response.errorBody()?.string()
                    }"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<RecipeDTO>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun approveCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit) {
        api.approveCreate(recipeId).enqueue(object : Callback<ApiResponseDto<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Boolean>>,
                response: Response<ApiResponseDto<Boolean>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Boolean>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun rejectCreateRecipe(recipeId: UUID, callback: (Result<Boolean>?) -> Unit) {
        api.rejectCreate(recipeId).enqueue(object : Callback<ApiResponseDto<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Boolean>>,
                response: Response<ApiResponseDto<Boolean>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Boolean>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun approveUpdateOrDeleteRecipe(
        recipeId: UUID,
        isDraft: Boolean,
        callback: (Result<Boolean>?) -> Unit
    ) {
        api.approveUpdateOrDelete(recipeId, isDraft).enqueue(object : Callback<ApiResponseDto<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Boolean>>,
                response: Response<ApiResponseDto<Boolean>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Boolean>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun rejectUpdateOrDeleteRecipe(
        recipeId: UUID,
        isDraft: Boolean,
        callback: (Result<Boolean>?) -> Unit
    ) {
        api.rejectUpdateOrDelete(recipeId, isDraft).enqueue(object : Callback<ApiResponseDto<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Boolean>>,
                response: Response<ApiResponseDto<Boolean>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Boolean>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
