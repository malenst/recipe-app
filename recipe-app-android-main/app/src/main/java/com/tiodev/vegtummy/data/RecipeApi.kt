package com.tiodev.vegtummy.data

import com.tiodev.vegtummy.domain.Category
import com.tiodev.vegtummy.domain.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface RecipeApi {

    @GET("recipe")
    fun getRecipes() : Call<ApiResponseDto<List<Recipe>>>

    @GET("recipe/popular")
    fun getTop10Recipes() : Call<ApiResponseDto<List<Recipe>>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<Recipe>>

    @GET("recipe")
    fun getRecipesByCategory(@Query("category")category : String) : Call<ApiResponseDto<List<Recipe>>>

    @GET("category")
    fun getCategories() : Call<ApiResponseDto<List<Category>>>
}