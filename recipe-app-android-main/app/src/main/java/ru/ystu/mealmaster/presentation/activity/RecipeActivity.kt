package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import java.util.*

class RecipeActivity : AppCompatActivity() {
    private var img: ImageView? = null
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
        setContentView(R.layout.activity_recipe)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        getRecipeById()
        // Find views
        img = findViewById(R.id.recipe_img)
        txt = findViewById(R.id.tittle)
        description = findViewById(R.id.description)
        ing = findViewById(R.id.ing)
        time = findViewById(R.id.time)
        ratingBar = findViewById(R.id.ratingBar2)
        stepBtn = findViewById(R.id.steps_btn)
        reviews = findViewById(R.id.recipeReviews)
        ing_btn = findViewById(R.id.ing_btn)
        rew_btn = findViewById(R.id.floatingActionButton)
        backBtn = findViewById(R.id.back_btn)
        steps = findViewById(R.id.steps_txt)
        scrollView = findViewById(R.id.ing_scroll)
        scrollView_step = findViewById(R.id.steps)
        overlay = findViewById(R.id.image_gradient)

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

//        // Set recipe ingredients
//        ingList = getIntent().getStringExtra("ing").split("\n");
        // Set time
//        time.setText(ingList[0]);


//        for (int i = 1; i<ingList.length; i++){
//            ing.setText(ing.getText()+"\uD83D\uDFE2  "+ingList[i]+"\n");
        /*if(ingList[i].startsWith(" ")){
                ing.setText(ing.getText()+"\uD83D\uDFE2  "+ingList[i].trim().replaceAll("\\s{2,}", " ")+"\n");
            }else{

            }*/
//
//        }
        // Set recipe steps
//        steps?.setText(intent.getStringExtra("des"))
        // steps.setText(Html.fromHtml(getIntent().getStringExtra("des")));
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

        rew_btn.setOnClickListener{
            intent = Intent(this@RecipeActivity, AddReviewActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeIdString)
            this@RecipeActivity.startActivity(intent)
        }


        // Exit activity
        backBtn?.setOnClickListener {
            intent = Intent(this@RecipeActivity, HomeActivity::class.java)
            this@RecipeActivity.startActivity(intent)
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
                    Log.d("KARTOFEL", recipe.steps.entries.toString())
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