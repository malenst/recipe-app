package ru.ystu.mealmaster.data

import retrofit2.Call
import retrofit2.http.*
import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.Review
import ru.ystu.mealmaster.domain.ReviewData
import java.util.*

interface RecipeApiService {

    @GET("recipe")
    fun getRecipes() : Call<ApiResponseDto<List<Recipe>>>
    @GET("moderation/recipe/pending")
    fun getUncheckedRecipes() : Call<ApiResponseDto<List<Recipe>>>

    @GET("recipe/popular")
    fun getTop10Recipes() : Call<ApiResponseDto<List<Recipe>>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<Recipe>>

    @GET("recipe")
    fun getRecipesByCategory(@Query("category") category : String) : Call<ApiResponseDto<List<Recipe>>>

    @GET("category")
    fun getCategories() : Call<ApiResponseDto<List<Category>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("add-review/recipe/{id}")
    fun addReview(@Path("id") id: UUID, @Body body: ReviewData) : Call<ApiResponseDto<Review>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun login(
        @Query("username") username : String,
        @Query("password") password : String
    ) : Call<ApiResponseDto<List<Recipe>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("recipe")
    fun addRecipe(
        @Body recipe: Recipe
    ) : Call<ApiResponseDto<Recipe>>

    @GET("account/role")
    fun getCurrentUserRole() : Call<ApiResponseDto<String>>
}