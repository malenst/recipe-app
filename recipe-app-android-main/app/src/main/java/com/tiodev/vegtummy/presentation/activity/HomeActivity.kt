package com.tiodev.vegtummy.presentation.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.tiodev.vegtummy.R
import com.tiodev.vegtummy.SearchActivity
import com.tiodev.vegtummy.data.RecipeApiService
import com.tiodev.vegtummy.data.RecipeRepositoryImpl
import com.tiodev.vegtummy.databinding.ActivityHomeBinding
import com.tiodev.vegtummy.presentation.adapter.CatRecipeAdapter
import com.tiodev.vegtummy.presentation.adapter.PopRecipeAdapter
import com.tiodev.vegtummy.presentation.adapter.RecipeAdapter
import com.tiodev.vegtummy.presentation.viewModel.CatRecipeViewModel
import com.tiodev.vegtummy.presentation.viewModel.CatRecipeViewModelFactory
import com.tiodev.vegtummy.presentation.viewModel.PopRecipeViewModel
import com.tiodev.vegtummy.presentation.viewModel.PopRecipeViewModelFactory
import com.tiodev.vegtummy.presentation.viewModel.RecipeViewModel
import com.tiodev.vegtummy.presentation.viewModel.RecipeViewModelFactory

class HomeActivity : AppCompatActivity() {
    private var salad: ImageView? = null
    private var main: ImageView? = null
    private var drinks: ImageView? = null
    private var dessert: ImageView? = null
    private var recyclerViewHome: RecyclerView? = null
    private var popRecyclerViewHome: RecyclerView? = null
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var popRecipeAdapter: PopRecipeAdapter
    private lateinit var catRecipeAdapter: CatRecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var popRecipeViewModel: PopRecipeViewModel
    private lateinit var catRecipeViewModel: CatRecipeViewModel
    private lateinit var binding: ActivityHomeBinding

    private var lottie: LottieAnimationView? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_home)

        // Find views
//        salad = findViewById(R.id.salad)
//        main = findViewById(R.id.MainDish)
//        drinks = findViewById(R.id.Drinks)
//        dessert = findViewById(R.id.Desserts)
        recyclerViewHome = findViewById(R.id.recview)
        popRecyclerViewHome = findViewById(R.id.rcview_popular)
        lottie = findViewById(R.id.lottie)
        editText = findViewById(R.id.editText)

        // Set all recipes
        setAllRecipesList()
        setPopularRecipesList()
        setCategoriesList()

        // Category buttons - start new activity with intent method "start"
//        salad!!.setOnClickListener { start("Salad", "Salad") }
//        main!!.setOnClickListener { start("Dish", "Main dish") }
//        drinks!!.setOnClickListener { start("Drinks", "Drinks") }
//        dessert!!.setOnClickListener { start("Desserts", "Dessert") }

        // Open search activity
        editText!!.setOnClickListener {
            val intent = Intent(this@HomeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setAllRecipesList() {
        setContentView(binding.root)

        val api = RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)

        recipeViewModel = ViewModelProvider(
            this,
            RecipeViewModelFactory(repository)
        )[RecipeViewModel::class.java]

        recipeAdapter = RecipeAdapter(emptyList())

        binding.recview.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = recipeAdapter
        }

        recipeViewModel.recipes.observe(this@HomeActivity) { recipes ->
            recipes?.let {
                recipeAdapter.updateData(it)
            }
        }

        // Hide progress animation
        lottie?.setVisibility(View.GONE)
    }


    private fun setCategoriesList() {
        setContentView(binding.root)

        val api = RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)

        catRecipeViewModel = ViewModelProvider(
            this,
            CatRecipeViewModelFactory(repository)
        )[CatRecipeViewModel::class.java]

        catRecipeAdapter = CatRecipeAdapter(emptyList())

        binding.categories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = catRecipeAdapter
        }

        catRecipeViewModel.categories.observe(this@HomeActivity) { categories ->
            categories?.let {
                catRecipeAdapter.updateData(it)
            }
        }

        // Hide progress animation
        lottie?.setVisibility(View.GONE)
    }

    private fun setPopularRecipesList() {
        val api = RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)
        popRecipeAdapter = PopRecipeAdapter(emptyList())
        popRecipeViewModel = ViewModelProvider(
            this,
            PopRecipeViewModelFactory(repository)
        )[PopRecipeViewModel::class.java]
        binding.rcviewPopular.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popRecipeAdapter
        }

        popRecipeViewModel.popRecipes.observe(this@HomeActivity) { recipes ->
            recipes?.let {
                popRecipeAdapter.updateData(it)
            }
        }
    }

    // Start MainActivity(Recipe list) with intent message
    private fun start(p: String?, tittle: String?) {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        intent.putExtra("Category", p)
        intent.putExtra("tittle", tittle)
        startActivity(intent)
    }


    // Create a bottom dialog for privacy policy and about
    private fun showBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)

        val abtDev = dialog.findViewById<LinearLayout>(R.id.about_dev)

        abtDev.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(getString(R.string.abt_dev)))
            startActivity(intent)
        }

    }
}