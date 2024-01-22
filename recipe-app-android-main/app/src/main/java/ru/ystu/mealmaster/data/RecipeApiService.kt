package ru.ystu.mealmaster.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.ystu.mealmaster.domain.*
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

    @DELETE("recipe/{id}")
    fun deleteRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<Boolean>>

    @GET("recipe")
    fun getRecipesByCategory(@Query("category") category : String) : Call<ApiResponseDto<List<Recipe>>>

    @GET("user/@{username}/recipe")
    fun getRecipesByUser(@Path("username") username: String) : Call<ApiResponseDto<List<Recipe>>>

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

    @GET("oauth2/authorization/VK")
    fun loginWithVK() : Call<ResponseBody>

    @POST("logout")
    fun logout() : Call<String>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("register")
    fun register(
        @Body registrationRequestDTO: RegistrationRequestDTO
    ) : Call<ApiResponseDto<User>>

    @GET("account")
    fun getAccountInfo() : Call<ApiResponseDto<User>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("recipe")
    fun addRecipe(
        @Body recipe: RecipeData
    ) : Call<ApiResponseDto<Recipe>>

    @GET("me/role")
    fun getCurrentUserRole() : Call<ApiResponseDto<String>>

    @GET("reviews/{id}")
    fun getReviewsById(@Path("id") id : UUID) : Call<ApiResponseDto<List<Review>>>

    @POST("log/recipe/{id}")
    fun logViewToRecipeById(@Path("id") id : UUID) : Call<ApiResponseDto<Recipe>>
}