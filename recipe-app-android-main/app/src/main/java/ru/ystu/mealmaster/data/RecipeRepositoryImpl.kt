package ru.ystu.mealmaster.data

import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.RecipeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class RecipeRepositoryImpl(private val api: ru.ystu.mealmaster.data.RecipeApi) : RecipeRepository {
    override fun getRecipes(callback: (Result<List<Recipe>>?) -> Unit) {
        api.getRecipes().enqueue(object : Callback<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>,
                response: Response<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipeById(id: UUID, callback: (Result<Recipe>?) -> Unit) {
        api.getRecipeById(id).enqueue(object : Callback<ru.ystu.mealmaster.data.ApiResponseDto<Recipe>> {
            override fun onResponse(
                call: Call<ru.ystu.mealmaster.data.ApiResponseDto<Recipe>>,
                response: Response<ru.ystu.mealmaster.data.ApiResponseDto<Recipe>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!.response))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ru.ystu.mealmaster.data.ApiResponseDto<Recipe>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }


    override fun getTop10Recipes(callback: (Result<List<Recipe>>?) -> Unit) {
        api.getTop10Recipes().enqueue(object : Callback<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>,
                response: Response<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getCategories(callback: (Result<List<Category>>?) -> Unit) {
        api.getCategories().enqueue(object : Callback<ru.ystu.mealmaster.data.ApiResponseDto<List<Category>>> {
            override fun onResponse(
                call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Category>>>,
                response: Response<ru.ystu.mealmaster.data.ApiResponseDto<List<Category>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Category>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    override fun getRecipesByCategory(category: String, callback: (Result<List<Recipe>>?) -> Unit) {
        api.getRecipesByCategory(category).enqueue(object : Callback<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>,
                response: Response<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()?.response ?: emptyList()))
                } else {
                    callback(Result.failure(Exception("Ошибка запроса: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

}
