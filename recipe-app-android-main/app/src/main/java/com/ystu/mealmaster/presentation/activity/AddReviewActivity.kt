package com.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ystu.mealmaster.R
import com.ystu.mealmaster.data.ApiResponseDto
import com.ystu.mealmaster.data.RecipeApi
import com.ystu.mealmaster.data.RecipeApiService
import com.ystu.mealmaster.domain.Recipe
import com.ystu.mealmaster.domain.Review
import com.ystu.mealmaster.domain.ReviewData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddReviewActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var api: RecipeApi
    private lateinit var editTextReview: EditText
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_review)
        api = RecipeApiService.api

        backBtn = findViewById(R.id.backBtnAddReview)
        editTextReview = findViewById(R.id.editTextReview)
        ratingBar = findViewById(R.id.ratingBar)

        val recipeId = intent.getStringExtra("RECIPE_ID") ?: return

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val reviewText = editTextReview.text.toString()
            val rating = ratingBar.rating

            submitReview(UUID.fromString(recipeId), reviewText, rating.toInt())
        }

        backBtn.setOnClickListener { finish() }
    }

    private fun submitReview(recipeId: UUID, reviewText: String, rating: Int) {
        val reviewData = ReviewData(reviewText, rating)

        // TODO: использовать данные пользователя
        api.login("admin", "admin").enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful) {
                    Log.d("LOGIN", (response.body()?.response.toString()))
                    api.addReview(recipeId, reviewData).enqueue(object : Callback<ApiResponseDto<Review>> {
                        override fun onResponse(call: Call<ApiResponseDto<Review>>, response: Response<ApiResponseDto<Review>>) {
                            if (response.isSuccessful) {
                                Log.d("REVIEW", response.body()?.response.toString())
                                showReviewThanksDialog(recipeId)
                            } else {
                                Log.d("REVIEW", "Неуспешно")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponseDto<Review>>, t: Throwable) {
                            Log.e("REVIEW", "Ошибка при запросе")
                            Log.e("REVIEW", t.stackTraceToString())
                        }
                    })
                } else {
                    Log.d("REVIEW", "Неуспешно")
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                Log.e("LOGIN", t.stackTraceToString())
            }
        })
    }

    private fun showReviewThanksDialog(recipeId: UUID) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Спасибо за отзыв!")
            .setMessage("Ваш отзыв успешно отправлен")
            .setPositiveButton("ОК") { dialog, which ->
                navigateToRecipeActivity(recipeId)
            }
            .create()

        alertDialog.show()
    }

    private fun navigateToRecipeActivity(recipeId: UUID) {
        val intent = Intent(this, RecipeActivity::class.java).apply {
            putExtra("RECIPE_ID", recipeId.toString())
        }
        startActivity(intent)
    }
}