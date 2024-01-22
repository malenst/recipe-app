package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityRecipeModerationBinding
import ru.ystu.mealmaster.domain.enumeration.ChangeType
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.ReviewAdapter
import ru.ystu.mealmaster.presentation.viewmodel.ReviewViewModel
import ru.ystu.mealmaster.presentation.viewmodel.ReviewViewModelFactory
import ru.ystu.mealmaster.util.RecipeUtils
import java.util.*

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
    private var action_btn: Button? = null
    private var reject_btn: Button? = null

    @Suppress("unused")
    var isImgCrop = false
    private var scrollView: ScrollView? = null
    private var scrollView_step: ScrollView? = null

    @Suppress("unused")
    private lateinit var context: Context

    private lateinit var recipeIdString: String
    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor
    private lateinit var binding: ActivityRecipeModerationBinding
    private lateinit var reviewViewModel: ReviewViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeModerationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        getRecipeById()
        logViewToRecipeById()
        setAllReviewsList()

        // Find views
        img = findViewById(R.id.recipe_img_moderRecipe)
        txt = findViewById(R.id.tittle_moderRecipe)
        author = findViewById(R.id.author_moderRecipe)
        description = findViewById(R.id.description_moderRecipe)
        ing = findViewById(R.id.ing_moderRecipe)
        time = findViewById(R.id.time_moderRecipe)
        views = findViewById(R.id.recipeViewText_moderRecipe)
        amount = findViewById(R.id.nutritionalAmountValue)
        measureUnit = findViewById(R.id.nutritionalMeasureUnitValue)
        calories = findViewById(R.id.nutritionalCaloriesValue)
        proteins = findViewById(R.id.nutritionalProteinValue)
        fats = findViewById(R.id.nutritionalFatsValue)
        carbohydrates = findViewById(R.id.nutritionalCarbohydratesValue)
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
        action_btn = findViewById(R.id.accept_btn_moderRecipe)
        reject_btn = findViewById(R.id.decline_btn_moderRecipe)

        Log.d("LUK SELENII", steps?.text.toString())
        stepBtn?.setTextColor(getColor(R.color.black))
        ing_btn?.setTextColor(getColor(R.color.white))

        //        scroll = findViewById(R.id.scroll);
//        zoomImage = findViewById(R.id.zoom_image);
        Log.d("QWER", intent.getStringExtra("CHANGE_TYPE")!!)
        val changeType: String = intent.getStringExtra("CHANGE_TYPE").toString()
        setListenersOnClicks(changeType)
//        when (intent.getStringExtra("CHANGE_TYPE")) {
//            "CREATE" -> {
//                Log.d("CREATE", "THIS IS CREATE")
//                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_add)
//                action_btn!!.text = ChangeType.CREATE.value
//
//                action_btn?.setOnClickListener {
//                    setActionButton("CREATE")
//                    intent = Intent(this@ModRecipeActivity, ModerationActivity::class.java)
//                    this@ModRecipeActivity.startActivity(intent)
//                }
//            }
//
//            "UPDATE" -> {
//                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_update)
//                action_btn!!.text = ChangeType.UPDATE.value
//
//                action_btn?.setOnClickListener {
//                    setActionButton("UPDATE")
//                    intent = Intent(this@ModRecipeActivity, ModerationActivity::class.java)
//                    this@ModRecipeActivity.startActivity(intent)
//                }
//            }
//
//            "DELETE" -> {
//                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_delete)
//                action_btn!!.text = ChangeType.DELETE.value
//
//                action_btn?.setOnClickListener {
//                    setActionButton("DELETE")
//                    intent = Intent(this@ModRecipeActivity, ModerationActivity::class.java)
//                    this@ModRecipeActivity.startActivity(intent)
//                }
//            }
//        }

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

        backBtn?.setOnClickListener {
            intent = Intent(this@ModRecipeActivity, HomeActivity::class.java)
            this@ModRecipeActivity.startActivity(intent)
        }

    }

    private fun setListenersOnClicks(changeType: String) {
        when(changeType) {
            "CREATE" -> {
                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_add)
                action_btn!!.text = ChangeType.CREATE.value
            }
            "UPDATE" -> {
                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_update)
                action_btn!!.text = ChangeType.UPDATE.value
            }
            "DELETE" -> {
                action_btn!!.background = ContextCompat.getDrawable(this, R.drawable.status_delete)
                action_btn!!.text = ChangeType.DELETE.value
            }
        }

        action_btn?.setOnClickListener {
            setActionButton(changeType)
            intent = Intent(this@ModRecipeActivity, ModerationActivity::class.java)
            this@ModRecipeActivity.startActivity(intent)
        }

        reject_btn?.setOnClickListener {
            setRejectButton(changeType)
            intent = Intent(this@ModRecipeActivity, ModerationActivity::class.java)
            this@ModRecipeActivity.startActivity(intent)
        }
    }

    private fun setActionButton(action: String) {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID")
                    ?: throw IllegalArgumentException("Recipe ID not found.")

                when (action) {
                    "CREATE" -> {
                            interactor.approveCreateRecipe(UUID.fromString(recipeIdString))
                    }
                    "UPDATE", "DELETE" -> {
                        interactor.approveUpdateOrDeleteRecipe(UUID.fromString(recipeIdString), true)
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    private fun setRejectButton(action: String) {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID")
                    ?: throw IllegalArgumentException("Recipe ID not found.")

                when (action) {
                    "CREATE" -> {
                            interactor.rejectCreateRecipe(UUID.fromString(recipeIdString))
                    }
                    "UPDATE", "DELETE" -> {
                        interactor.rejectUpdateOrDeleteRecipe(UUID.fromString(recipeIdString), true)
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }
    private fun logViewToRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID")
                    ?: throw IllegalArgumentException("Recipe ID not found.")
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
                recipeIdString = intent.extras?.getString("RECIPE_ID")
                    ?: throw IllegalArgumentException("Recipe ID not found.")
                val recipeFromDb = interactor.getRecipeById(UUID.fromString(recipeIdString))

                recipeFromDb.let { recipe ->
                    val ingredientsFormatted =
                        recipe.ingredients.entries.joinToString(separator = "\n") { (key, value) ->
                            "• $key ― $value"
                        }
                    ing?.text = ingredientsFormatted

                    val stepsFormatted =
                        recipe.steps.entries.joinToString(separator = "\n") { (key, value) ->
                            "$key) $value"
                        }
                    steps?.text = stepsFormatted

                    time?.text = recipe.cookingTime
                    description?.text = recipe.description
                    author?.text = recipe.author

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

        binding.reviewRecviewModerRecipe.apply {
            layoutManager = LinearLayoutManager(this@ModRecipeActivity)
            adapter = reviewAdapter
        }

        reviewViewModel.reviews.observe(this@ModRecipeActivity) { recipes ->
            recipes?.let {
                Log.d("GGG", it.toString())
                reviewAdapter.updateData(it)
            }
        }

    }

    private fun reloadData() {
        getRecipeById()
        setAllReviewsList()
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }
}