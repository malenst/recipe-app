package ru.ystu.mealmaster.data

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ystu.mealmaster.domain.*
import ru.ystu.mealmaster.util.sharedpref.SharedPrefManager
import java.util.*


class RecipeRepositoryImpl(private val api: RecipeApiService, private val context: Context) : RecipeRepository {
    override fun getRecipes(callback: (Result<List<Recipe>>?) -> Unit) {
        api.getRecipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
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

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getUncheckedRecipes(callback: (Result<List<Recipe>>?) -> Unit) {
        api.getUncheckedRecipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
                    Log.d("APELSIN", response.body()?.response.toString())
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    Log.d("MANGO", "MANGO")
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {

                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipeById(id: UUID, callback: (Result<Recipe>?) -> Unit) {
        api.getRecipeById(id).enqueue(object : Callback<ApiResponseDto<Recipe>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Recipe>>,
                response: Response<ApiResponseDto<Recipe>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Recipe>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }


    override fun getTop10Recipes(callback: (Result<List<Recipe>>?) -> Unit) {
        api.getTop10Recipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getCategories(callback: (Result<List<Category>>?) -> Unit) {
        api.getCategories().enqueue(object : Callback<ApiResponseDto<List<Category>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Category>>>,
                response: Response<ApiResponseDto<List<Category>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Category>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipesByCategory(category: String, callback: (Result<List<Recipe>>?) -> Unit) {
        api.getRecipesByCategory(category).enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun login(username: String, password: String, callback: (Result<List<Recipe>>, List<String>?) -> Unit) {
        api.login(username, password).enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
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

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t), emptyList())
            }
        })
    }

    override fun register(registrationRequestDTO: RegistrationRequestDTO, callback: (Result<User>) -> Unit) {
        api.register(registrationRequestDTO).enqueue(object : Callback<ApiResponseDto<User>> {
            override fun onResponse(
                call: Call<ApiResponseDto<User>>,
                response: Response<ApiResponseDto<User>>
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

            override fun onFailure(call: Call<ApiResponseDto<User>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun addRecipe(recipe: RecipeData, callback: (Result<Recipe>?) -> Unit) {
        api.addRecipe(recipe).enqueue(object : Callback<ApiResponseDto<Recipe>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Recipe>>,
                response: Response<ApiResponseDto<Recipe>>
            ) {
                if (response.isSuccessful || response.code() == 201 || response.code() == 301 || response.code() == 302) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Recipe>>, t: Throwable) {
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

            override fun onFailure(call: Call<ApiResponseDto<String>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun logViewToRecipeById(id: UUID, callback: (Result<Recipe>) -> Unit) {
        api.logViewToRecipeById(id).enqueue(object : Callback<ApiResponseDto<Recipe>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Recipe>>,
                response: Response<ApiResponseDto<Recipe>>
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

            override fun onFailure(call: Call<ApiResponseDto<Recipe>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
