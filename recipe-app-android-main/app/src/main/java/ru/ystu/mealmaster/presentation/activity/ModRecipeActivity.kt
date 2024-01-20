package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.RecipeRepository
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import java.util.UUID

class ModRecipeActivity : AppCompatActivity() {
    private var img: ImageView? = null
    private var rewievRecycleView: RecyclerView? = null
    private var backBtn: ImageView? = null
    private var overlay: ImageView? = null
    @Suppress("unused")
    var scroll: ImageView? = null
    @Suppress("unused")
    var zoomImage: ImageView? = null
    private var txt: TextView? = null
    private var ing: TextView? = null
    private var description: TextView? = null
    private var time: TextView? = null
    private var views: TextView? = null
    private var ratingBar: RatingBar? = null
    private var steps: TextView? = null
    private var reviews: TextView? = null
    private var stepBtn: Button? = null
    private var ing_btn: Button? = null
    private lateinit var rew_btn: FloatingActionButton
    @Suppress("unused")
    var isImgCrop = false
    private var scrollView: ScrollView? = null
    private var scrollView_step: ScrollView? = null
    @Suppress("unused")
    private lateinit var context: Context

    private lateinit var recipeIdString: String

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_moderation)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        logViewToRecipeById()
        getRecipeById()

        // Find views
        img = findViewById(R.id.recipe_img_moderRecipe)
        txt = findViewById(R.id.tittle_moderRecipe)
        description = findViewById(R.id.description_moderRecipe)
        ing = findViewById(R.id.ing_moderRecipe)
        time = findViewById(R.id.time_moderRecipe)
        views = findViewById(R.id.recipeViewText_moderRecipe)
        ratingBar = findViewById(R.id.ratingBar2_moderRecipe)
        stepBtn = findViewById(R.id.steps_btn_moderRecipe)
        reviews = findViewById(R.id.recipeReviews_moderRecipe)
        ing_btn = findViewById(R.id.ing_btn_moderRecipe)
        backBtn = findViewById(R.id.back_btn_moderRecipe)
        steps = findViewById(R.id.steps_txt_moderRecipe)
        scrollView = findViewById(R.id.ing_scroll_moderRecipe)
        scrollView_step = findViewById(R.id.steps_moderRecipe)
        overlay = findViewById(R.id.image_gradient_moderRecipe)
        rewievRecycleView = findViewById(R.id.review_recview_moderRecipe)

        Log.d("LUK SELENII", steps?.text.toString())
        stepBtn?.setTextColor(getColor(R.color.black))
        ing_btn?.setTextColor(getColor(R.color.white))

        //        scroll = findViewById(R.id.scroll);
//        zoomImage = findViewById(R.id.zoom_image);

        // Load recipe image from link
        Glide.with(applicationContext).load(intent.getStringExtra("img"))
            .into(img!!)
        // Set recipe title
        txt?.text = intent.getStringExtra("tittle")

        stepBtn?.background = null
        stepBtn?.setOnClickListener {
            stepBtn?.setBackgroundResource(R.drawable.btn_ing)
            stepBtn?.setTextColor(getColor(R.color.white))
            ing_btn?.background = null
            ing_btn?.setTextColor(getColor(R.color.black))
            scrollView?.visibility = View.GONE
            scrollView_step?.visibility = View.VISIBLE
        }
        ing_btn?.setOnClickListener {
            ing_btn?.setBackgroundResource(R.drawable.btn_ing)
            ing_btn?.setTextColor(getColor(R.color.white))
            stepBtn?.background = null
            stepBtn?.setTextColor(getColor(R.color.black))
            scrollView?.visibility = View.VISIBLE
            scrollView_step?.visibility = View.GONE
        }


    }

    private fun logViewToRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID") ?: throw IllegalArgumentException("Recipe ID not found.")
                interactor.logViewToRecipeById(UUID.fromString(recipeIdString))
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    private fun getRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID") ?: throw IllegalArgumentException("Recipe ID not found.")
                val recipeFromDb = interactor.getRecipeById(UUID.fromString(recipeIdString))

                recipeFromDb.let { recipe ->
                    val ingredientsFormatted = recipe.ingredients.entries.joinToString(separator = "\n") { (key, value) ->
                        "• $key ― $value"
                    }
                    ing?.text = ingredientsFormatted

                    val stepsFormatted = recipe.steps.entries.joinToString(separator = "\n") { (key, value) ->
                        "$key) $value"
                    }
                    steps?.text = stepsFormatted

                    time?.text = recipe.cookingTime
                    description?.text = recipe.description

                    ratingBar?.rating = recipe.reviews?.map { it.rating }?.average()?.toFloat()!!
                    txt?.text = recipe.name
                    Log.d("RECIPE VIEWS", recipe.views.toString())
                    views?.text = recipe.views.toString()

                    val reviewsText = recipe.reviews.joinToString(separator = "\n\n") { review ->
                        "Автор: ${review.author}\nОтзыв: ${review.text}\nОценка: ${review.rating}\nВремя: ${review.date}"
                    }

                    if (reviewsText.isNotEmpty()) {
                        reviews?.text = reviewsText
                    }

                    if (!recipe.image.isNullOrEmpty()) {
                        val imageNorm = recipe.image
                            .replace("http", BuildConfig.BASE_PROTOCOL)
                            .replace("localhost", BuildConfig.BASE_HOST)
                            .replace("8080", BuildConfig.BASE_PORT)
                        Picasso.get().load(imageNorm).into(img)
                        img?.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    private fun reloadData() {
        getRecipeById()
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }
}