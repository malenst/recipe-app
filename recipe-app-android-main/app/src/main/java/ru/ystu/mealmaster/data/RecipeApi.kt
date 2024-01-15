package ru.ystu.mealmaster.data

import ru.ystu.mealmaster.domain.*
import retrofit2.Call
import retrofit2.http.*
import java.util.UUID

interface RecipeApi {

    @GET("recipe")
    fun getRecipes() : Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>

    @GET("recipe/popular")
    fun getTop10Recipes() : Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id : UUID) : Call<ru.ystu.mealmaster.data.ApiResponseDto<Recipe>>

    @GET("recipe")
    fun getRecipesByCategory(@Query("category") category : String) : Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>

    @GET("category")
    fun getCategories() : Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Category>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("add-review/recipe/{id}")
    fun addReview(@Path("id") id: UUID, @Body body: ReviewData) : Call<ru.ystu.mealmaster.data.ApiResponseDto<Review>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun login(
        @Query("username") username : String,
        @Query("password") password : String
    ) : Call<ru.ystu.mealmaster.data.ApiResponseDto<List<Recipe>>>
}