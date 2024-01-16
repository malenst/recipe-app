package ru.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityHomeBinding
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.presentation.adapter.CatRecipeAdapter
import ru.ystu.mealmaster.presentation.adapter.PopRecipeAdapter
import ru.ystu.mealmaster.presentation.adapter.RecipeAdapter
import ru.ystu.mealmaster.presentation.viewmodel.*

class HomeActivity : AppCompatActivity() {
    private var recyclerViewHome: RecyclerView? = null
    private var popRecyclerViewHome: RecyclerView? = null
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var popRecipeAdapter: PopRecipeAdapter
    private lateinit var catRecipeAdapter: CatRecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var popRecipeViewModel: PopRecipeViewModel
    private lateinit var catRecipeViewModel: CatRecipeViewModel
    private lateinit var profileButton: ImageView
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

        profileButton = findViewById(R.id.imageView4)
        profileButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // Open search activity
        editText!!.setOnClickListener {
            val intent = Intent(this@HomeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        setAllRecipesList()
        setPopularRecipesList()
        setCategoriesList()
    }

    private fun setAllRecipesList() {
        setContentView(binding.root)

        RecipeApiService.init(this)
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

        RecipeApiService.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApiService.api
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
        RecipeApiService.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApiService.api
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