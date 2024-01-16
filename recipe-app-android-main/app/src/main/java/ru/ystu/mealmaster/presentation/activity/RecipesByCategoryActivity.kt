package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityMainBinding
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.presentation.adapter.RecipeAdapter
import ru.ystu.mealmaster.presentation.adapter.RecipesByCategoryAdapter
import ru.ystu.mealmaster.presentation.viewmodel.RecipeViewModel
import ru.ystu.mealmaster.presentation.viewmodel.RecipeViewModelFactory
import ru.ystu.mealmaster.presentation.viewmodel.RecipesByCategoryViewModel
import ru.ystu.mealmaster.presentation.viewmodel.RecipesByCategoryViewModelFactory

class RecipesByCategoryActivity : AppCompatActivity() {
    private lateinit var recipesByCategoryAdapter: RecipesByCategoryAdapter
    private lateinit var recipesByCategoryViewModel: RecipesByCategoryViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RecipeApiService.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)
        val interactor = RecipeInteractorImpl(repository)

        val category = intent.extras?.getString("CATEGORY")
        val translation = intent.extras?.getString("TRANSLATION")
        val titleTextView = findViewById<TextView>(R.id.activityMainTittle)

        recipesByCategoryAdapter = RecipesByCategoryAdapter(emptyList())
        if (!category.equals("Модерация")) {
            titleTextView.text = translation

            recipesByCategoryViewModel = ViewModelProvider(
                this,
                RecipesByCategoryViewModelFactory(interactor, category!!)
            )[RecipesByCategoryViewModel::class.java]

            recipesByCategoryViewModel = ViewModelProvider(
                this,
                RecipesByCategoryViewModelFactory(interactor, category!!)
            )[RecipesByCategoryViewModel::class.java]

            binding.mainRecview.apply {
                layoutManager = LinearLayoutManager(this@RecipesByCategoryActivity)
                adapter = recipesByCategoryAdapter
            }

            recipesByCategoryViewModel.recipesByCategory.observe(this@RecipesByCategoryActivity) { recipes ->
                recipes?.let {
                    recipesByCategoryAdapter.updateData(it)
                }
            }
        } else {
            titleTextView.text = "Модерация"

            recipeViewModel = ViewModelProvider(
                this,
                RecipeViewModelFactory(interactor)
            )[RecipeViewModel::class.java]

            recipeAdapter = RecipeAdapter(emptyList())

            binding.mainRecview.apply {
                layoutManager = LinearLayoutManager(this@RecipesByCategoryActivity)
                adapter = recipeAdapter
            }

            recipeViewModel.recipes.observe(this@RecipesByCategoryActivity) { recipes ->
                recipes?.let {
                    recipeAdapter.updateData(it)
                }
            }
        }


        val backBtn : ImageView = findViewById(R.id.backBtnMain)
        // Exit activity
        backBtn.setOnClickListener { finish() }
    }
}