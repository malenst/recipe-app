package com.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ystu.mealmaster.R
import com.ystu.mealmaster.data.RecipeApiService
import com.ystu.mealmaster.data.RecipeRepositoryImpl
import com.ystu.mealmaster.databinding.ActivityHomeBinding
import com.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import com.ystu.mealmaster.presentation.adapter.CatRecipeAdapter
import com.ystu.mealmaster.presentation.adapter.PopRecipeAdapter
import com.ystu.mealmaster.presentation.adapter.RecipeAdapter
import com.ystu.mealmaster.presentation.viewmodel.CatRecipeViewModel
import com.ystu.mealmaster.presentation.viewmodel.CatRecipeViewModelFactory
import com.ystu.mealmaster.presentation.viewmodel.PopRecipeViewModel
import com.ystu.mealmaster.presentation.viewmodel.PopRecipeViewModelFactory
import com.ystu.mealmaster.presentation.viewmodel.RecipeViewModel
import com.ystu.mealmaster.presentation.viewmodel.RecipeViewModelFactory

class HomeActivity : AppCompatActivity() {
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

        recyclerViewHome = findViewById(R.id.recview)
        popRecyclerViewHome = findViewById(R.id.rcview_popular)
        lottie = findViewById(R.id.lottie)
        editText = findViewById(R.id.editText)

        // Set all recipes
        setAllRecipesList()
        setPopularRecipesList()
        setCategoriesList()

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
        val interactor = RecipeInteractorImpl(repository)

        recipeViewModel = ViewModelProvider(
            this,
            RecipeViewModelFactory(interactor)
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
        val interactor = RecipeInteractorImpl(repository)

        catRecipeViewModel = ViewModelProvider(
            this,
            CatRecipeViewModelFactory(interactor)
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
        val interactor = RecipeInteractorImpl(repository)

        popRecipeAdapter = PopRecipeAdapter(emptyList())
        popRecipeViewModel = ViewModelProvider(
            this,
            PopRecipeViewModelFactory(interactor)
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
}