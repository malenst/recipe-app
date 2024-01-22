package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.database.FavouriteRecipeDao
import ru.ystu.mealmaster.data.database.FavouriteRecipeDatabase
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.data.repository.FavouriteRecipeRepositoryImpl
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityRecipeBinding
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractor
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractorImpl
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.FavouriteRecipeRepository
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.ReviewAdapter
import ru.ystu.mealmaster.presentation.viewmodel.ReviewViewModel
import ru.ystu.mealmaster.presentation.viewmodel.ReviewViewModelFactory
import ru.ystu.mealmaster.util.RecipeUtils
import java.util.*

class RecipeActivity : AppCompatActivity() {
    private var img: ImageView? = null
    private var rewievRecycleView: RecyclerView? = null
    private var backBtn: ImageView? = null
    private var overlay: ImageView? = null

    @Suppress("unused")
    var scroll: ImageView? = null

    @Suppress("unused")
    var zoomImage: ImageView? = null
    private var txt: TextView? = null
    private var author: TextView? = null
    private var ing: TextView? = null
    private var description: TextView? = null
    private var time: TextView? = null
    private var views: TextView? = null
    private var amount: TextView? = null
    private var measureUnit: TextView? = null
    private var calories: TextView? = null
    private var proteins: TextView? = null
    private var fats: TextView? = null
    private var carbohydrates: TextView? = null
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

    private lateinit var dao: FavouriteRecipeDao
    private lateinit var favRepository: FavouriteRecipeRepository
    private lateinit var favInteractor: FavouriteRecipeInteractor
    private var currentUserUsername: String? = null
    private var currentUserRole: String? = null
    private lateinit var favIcon: ImageView

    private lateinit var recipeIdString: String
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var reviewViewModel: ReviewViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        getRecipeById()
        logViewToRecipeById()
        setAllReviewsList()

        // Find views
        img = findViewById(R.id.recipe_img)
        txt = findViewById(R.id.tittle)
        author = findViewById(R.id.author)
        description = findViewById(R.id.description)
        ing = findViewById(R.id.ing)
        time = findViewById(R.id.time)
        views = findViewById(R.id.recipeViewText)
        amount = findViewById(R.id.nutritionalAmountValue)
        measureUnit = findViewById(R.id.nutritionalMeasureUnitValue)
        calories = findViewById(R.id.nutritionalCaloriesValue)
        proteins = findViewById(R.id.nutritionalProteinValue)
        fats = findViewById(R.id.nutritionalFatsValue)
        carbohydrates = findViewById(R.id.nutritionalCarbohydratesValue)
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
        rewievRecycleView = findViewById(R.id.review_recview)

        Log.d("LUK SELENII", steps?.text.toString())
        stepBtn?.setTextColor(getColor(R.color.black))
        ing_btn?.setTextColor(getColor(R.color.white))

        favIcon = findViewById(R.id.image_favourite)
        lifecycleScope.launch {
            currentUserUsername = interactor.getAccountInfo().username
            Log.d("XYU", currentUserUsername.toString())
            currentUserRole = interactor.getCurrentUserRole()

            favIcon.setOnClickListener {
                if (currentUserRole != "ANONYMOUS" && currentUserUsername != null) {
                    toggleFavourite()
                } else {
                    val intent = Intent(this@RecipeActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        //        scroll = findViewById(R.id.scroll);
//        zoomImage = findViewById(R.id.zoom_image);

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

        rew_btn.setOnClickListener {
            intent = Intent(this@RecipeActivity, AddReviewActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeIdString)
            this@RecipeActivity.startActivity(intent)
        }

        dao = FavouriteRecipeDatabase.getInstance(application)?.favouriteRecipeDao()!!
        //lifecycleScope.launch(Dispatchers.IO) { dao.clearFavouriteRecipes() }
        favRepository = FavouriteRecipeRepositoryImpl(dao)
        favInteractor = FavouriteRecipeInteractorImpl(favRepository)

        checkFavouriteStatusAndUpdateIcon()

        // Exit activity
        backBtn?.setOnClickListener {
            intent = Intent(this@RecipeActivity, HomeActivity::class.java)
            this@RecipeActivity.startActivity(intent)
        }
    }

    private fun logViewToRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString =
                    intent.extras?.getString("RECIPE_ID") ?: throw IllegalArgumentException("Recipe ID not found.")
                interactor.logViewToRecipeById(UUID.fromString(recipeIdString))
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString =
                    intent.extras?.getString("RECIPE_ID") ?: throw IllegalArgumentException("Recipe ID not found.")
                val recipeFromDb = interactor.getRecipeById(UUID.fromString(recipeIdString))

                recipeFromDb.let { recipe ->
                    val ingredientsFormatted =
                        recipe.ingredients.entries.joinToString(separator = "\n") { (key, value) ->
                            "• $key ― $value"
                        }
                    ing?.text = ingredientsFormatted

                    val stepsFormatted = recipe.steps.entries.joinToString(separator = "\n") { (key, value) ->
                        "$key) $value"
                    }
                    steps?.text = stepsFormatted

                    time?.text = recipe.cookingTime
                    description?.text = recipe.description
                    author?.text = recipe.author
                    author?.setOnClickListener {
                        lifecycleScope.launch {
                            if (currentUserUsername == author!!.text) {
                                val intent = Intent(this@RecipeActivity, MyRecipesActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@RecipeActivity, AuthorRecipesActivity::class.java)
                                intent.putExtra("AUTHOR_USERNAME", author!!.text)
                                startActivity(intent)
                            }
                        }
                    }

                    amount?.text = recipe.nutritionalInfo.amount.toString() + " "
                    measureUnit?.text = recipe.nutritionalInfo.measureUnit
                    calories?.text = recipe.nutritionalInfo.calories.toString()
                    proteins?.text = recipe.nutritionalInfo.protein.toString()
                    fats?.text = recipe.nutritionalInfo.fat.toString()
                    carbohydrates?.text = recipe.nutritionalInfo.carbohydrates.toString()

                    ratingBar?.rating = recipe.reviews?.map { it.rating }?.average()?.toFloat()!!
                    RecipeUtils.formatRecipeInfo(recipe, txt, views, reviews, img)
                }
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    private fun setAllReviewsList() {
        var id: UUID?
        try {
            id = UUID.fromString(intent.extras?.getString("RECIPE_ID"))
                ?: throw IllegalArgumentException("Recipe ID not found.")
        } catch (e: Exception) {
            Log.e("RecipeLoadError", "Error loading recipe", e)
            id = null
        }
        reviewViewModel = ViewModelProvider(
            this,
            ReviewViewModelFactory(interactor, id!!)
        )[ReviewViewModel::class.java]

        reviewAdapter = ReviewAdapter(emptyList())

        binding.reviewRecview.apply {
            layoutManager = LinearLayoutManager(this@RecipeActivity)
            adapter = reviewAdapter
        }

        reviewViewModel.reviews.observe(this@RecipeActivity) { recipes ->
            recipes?.let {
                reviewAdapter.updateData(it)
            }
        }

    }

    private fun toggleFavourite() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiRecipeId = UUID.fromString(recipeIdString)
                val currentUserUsername = interactor.getAccountInfo().username

                var favRecipe = favInteractor.getFavouriteRecipeById(apiRecipeId, currentUserUsername!!)

                if (favRecipe != null) {
                    favInteractor.deleteFavouriteRecipeById(apiRecipeId, currentUserUsername)
                } else {
                    val recipe = interactor.getRecipeById(apiRecipeId)
                    favInteractor.addFavouriteRecipe(FavouriteRecipe.fromRecipe(recipe, currentUserUsername))
                }

                favRecipe = favInteractor.getFavouriteRecipeById(apiRecipeId, currentUserUsername)
                updateFavIcon(favRecipe != null)

            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error in toggleFavourite", e)
            }
        }
    }


    private suspend fun updateFavIcon(isFavourite: Boolean) {
        withContext(Dispatchers.Main) {
            favIcon.let {
                if (isFavourite) {
                    it.setBackgroundResource(R.drawable.btn_favourite_green)
                } else {
                    it.setBackgroundResource(R.drawable.btn_favourite)
                }
            }
        }
    }

    private fun checkFavouriteStatusAndUpdateIcon() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiRecipeId = UUID.fromString(recipeIdString)
                val currentUserUsername = interactor.getAccountInfo().username

                val favRecipe = favInteractor.getFavouriteRecipeById(apiRecipeId, currentUserUsername!!)
                updateFavIcon(favRecipe != null)
            } catch (ignored: JsonSyntaxException) {
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error in checkFavouriteStatusAndUpdateIcon", e)
            }
        }
    }

    private fun reloadData() {
        getRecipeById()
        setAllReviewsList()
        checkFavouriteStatusAndUpdateIcon()
    }

    private fun setupFavIconClickListener() {
        lifecycleScope.launch {
            currentUserUsername = interactor.getAccountInfo().username
            currentUserRole = interactor.getCurrentUserRole()
            if (currentUserRole != "ANONYMOUS") {
                favIcon.setOnClickListener {
                    toggleFavourite()
                }
            } else {
                favIcon.setOnClickListener {
                    val intent = Intent(this@RecipeActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadData()
        setupFavIconClickListener()
    }
}