package ru.ystu.mealmaster.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.ystu.mealmaster.domain.dto.ApiResponseDto
import ru.ystu.mealmaster.domain.*
import ru.ystu.mealmaster.domain.dto.*
import java.util.*

interface RecipeApiService {

    @GET("recipe")
    fun getRecipes() : Call<ApiResponseDto<List<RecipeDTO>>>
    @GET("moderation/recipe/pending")
    fun getUncheckedRecipes() : Call<ApiResponseDto<List<RecipeDTO>>>

    @GET("recipe/popular")
    fun getTop10Recipes() : Call<ApiResponseDto<List<RecipeDTO>>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<RecipeDTO>>

    @DELETE("recipe/{id}")
    fun deleteRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<Boolean>>

    @GET("recipe")
    fun getRecipesByCategory(@Query("category") category : String) : Call<ApiResponseDto<List<RecipeDTO>>>

    @GET("user/@{username}/recipe")
    fun getRecipesByUser(@Path("username") username: String, @Query("approvedOnly") approvedOnly: Boolean) : Call<ApiResponseDto<List<RecipeDTO>>>

    @GET("category")
    fun getCategories() : Call<ApiResponseDto<List<CategoryDTO>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("add-review/recipe/{id}")
    fun addReview(@Path("id") id: UUID, @Body body: ReviewData) : Call<ApiResponseDto<ReviewDTO>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun login(
        @Query("username") username : String,
        @Query("password") password : String
    ) : Call<ApiResponseDto<List<RecipeDTO>>>

    @GET("oauth2/authorization/VK")
    fun loginWithVK() : Call<ResponseBody>

    @POST("logout")
    fun logout() : Call<String>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("register")
    fun register(
        @Body registrationRequestDTO: RegistrationRequestDTO
    ) : Call<ApiResponseDto<UserDTO>>

    @GET("account")
    fun getAccountInfo() : Call<ApiResponseDto<UserDTO>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("recipe")
    fun addRecipe(
        @Body recipe: RecipeData
    ) : Call<ApiResponseDto<RecipeDTO>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("recipe/{recipeId}")
    fun updateRecipe(
        @Path("recipeId") recipeId: UUID, @Body recipe: RecipeData
    ) : Call<ApiResponseDto<RecipeDTO>>

    @GET("me/role")
    fun getCurrentUserRole() : Call<ApiResponseDto<String>>

    @GET("reviews/{id}")
    fun getReviewsById(@Path("id") id : UUID) : Call<ApiResponseDto<List<ReviewDTO>>>

    @POST("log/recipe/{id}")
    fun logViewToRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<RecipeDTO>>

    @POST("moderation/recipe/{recipeId}/approve")
    fun approveCreate(@Path("recipeId") id : UUID) : Call<ApiResponseDto<Boolean>>

    @POST("moderation/recipe/{recipeId}/reject")
    fun rejectCreate(@Path("recipeId") id : UUID) : Call<ApiResponseDto<Boolean>>

    @POST("moderation/recipe/{recipeId}/approve")
    fun approveUpdateOrDelete(@Path("recipeId") id : UUID, @Query("isDraft") isDraft: Boolean) : Call<ApiResponseDto<Boolean>>

    @POST("moderation/recipe/{recipeId}/reject")
    fun rejectUpdateOrDelete(@Path("recipeId") id : UUID, @Query("isDraft") isDraft: Boolean) : Call<ApiResponseDto<Boolean>>
}