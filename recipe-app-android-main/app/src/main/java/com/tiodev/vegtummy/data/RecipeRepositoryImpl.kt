package com.tiodev.vegtummy.data

import android.util.Log
import com.tiodev.vegtummy.domain.Category
import com.tiodev.vegtummy.domain.Recipe
import com.tiodev.vegtummy.domain.RecipeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class RecipeRepositoryImpl(private val api: RecipeApi) : RecipeRepository {

    override fun getRecipes(callback: (List<Recipe>?) -> Unit) {
        api.getRecipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    Log.d("POMIDOR1", response.body()?.response.toString())
                    callback(response.body()?.response ?: emptyList())
                } else {
                    Log.d("POMIDOR2", response.body()?.response.toString())
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                Log.d("POMIDOR3", t.stackTraceToString())
                callback(null)
            }
        })
    }

    override fun getRecipeById(id: UUID, callback: (Recipe?) -> Unit) {
        api.getRecipeById(id).enqueue(object : Callback<ApiResponseDto<Recipe>> {
            override fun onResponse(
                call: Call<ApiResponseDto<Recipe>>,
                response: Response<ApiResponseDto<Recipe>>
            ) {
                if (response.isSuccessful) {
                    Log.d("POMIDOR1", response.body()?.response.toString())
                    callback(response.body()?.response)
                } else {
                    Log.d("POMIDOR2", response.body()?.response.toString())
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<Recipe>>, t: Throwable) {
                Log.d("POMIDOR3", t.stackTraceToString())
                callback(null)
            }
        })
    }

    override fun getTop10Recipes(callback: (List<Recipe>?) -> Unit) {
        api.getTop10Recipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.response ?: emptyList())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(null)
            }
        })
    }

    override fun getCategories(callback: (List<Category>?) -> Unit) {
        api.getCategories().enqueue(object : Callback<ApiResponseDto<List<Category>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Category>>>,
                response: Response<ApiResponseDto<List<Category>>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.response ?: emptyList())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Category>>>, t: Throwable) {
                callback(null)
            }
        })
    }

    override fun getRecipesByCategory(category: String, callback: (List<Recipe>?) -> Unit) {
        api.getRecipesByCategory(category).enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.response ?: emptyList())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                callback(null)
            }
        })
    }

}
