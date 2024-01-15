package com.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ystu.mealmaster.R
import com.ystu.mealmaster.data.RecipeApiService
import com.ystu.mealmaster.data.RecipeRepositoryImpl
import com.ystu.mealmaster.databinding.ActivityRecipesByCategoryBinding
import com.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import com.ystu.mealmaster.presentation.adapter.RecipesByCategoryAdapter
import com.ystu.mealmaster.presentation.viewmodel.RecipesByCategoryViewModel
import com.ystu.mealmaster.presentation.viewmodel.RecipesByCategoryViewModelFactory

class RecipesByCategoryActivity : AppCompatActivity() {
    private lateinit var recipesByCategoryAdapter: RecipesByCategoryAdapter
    private lateinit var recipesByCategoryViewModel: RecipesByCategoryViewModel
    private lateinit var binding: ActivityRecipesByCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_by_category)

        binding = ActivityRecipesByCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)
        val interactor = RecipeInteractorImpl(repository)

        val category = intent.extras?.getString("CATEGORY")
        val translation = intent.extras?.getString("TRANSLATION")
        val titleTextView = findViewById<TextView>(R.id.activityByCategoryTitle)
        titleTextView.text = translation

        recipesByCategoryAdapter = RecipesByCategoryAdapter(emptyList())
        recipesByCategoryViewModel = ViewModelProvider(
            this,
            RecipesByCategoryViewModelFactory(interactor, category!!)
        )[RecipesByCategoryViewModel::class.java]

        binding.recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecipesByCategoryActivity)
            adapter = recipesByCategoryAdapter
        }

        recipesByCategoryViewModel.recipesByCategory.observe(this@RecipesByCategoryActivity) { recipes ->
            recipes?.let {
                recipesByCategoryAdapter.updateData(it)
            }
        }

        val backBtn : ImageView = findViewById(R.id.backBtnByCategory)
        // Exit activity
        backBtn.setOnClickListener { finish() }
    }
}