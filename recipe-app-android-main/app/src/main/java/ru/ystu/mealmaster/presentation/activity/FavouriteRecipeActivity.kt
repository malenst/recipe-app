package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.database.FavouriteRecipeDatabase
import ru.ystu.mealmaster.data.repository.FavouriteRecipeRepositoryImpl
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractor
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractorImpl
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.FavouriteRecipeAdapter
import ru.ystu.mealmaster.presentation.viewmodel.FavouriteRecipeViewModel
import ru.ystu.mealmaster.presentation.viewmodel.FavouriteRecipeViewModelFactory

class FavouriteRecipeActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private lateinit var favAdapter: FavouriteRecipeAdapter
    private lateinit var favViewModel: FavouriteRecipeViewModel

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private lateinit var favInteractor: FavouriteRecipeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_recipe)

        recyclerView = findViewById(R.id.favRecyclerView)

        val dao = FavouriteRecipeDatabase.getInstance(application)?.favouriteRecipeDao()
        val favRepository = FavouriteRecipeRepositoryImpl(dao!!)
        favInteractor = FavouriteRecipeInteractorImpl(favRepository)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        lifecycleScope.launch {
            val belongsTo = interactor.getAccountInfo().username!!

            favViewModel = ViewModelProvider(
                this@FavouriteRecipeActivity,
                FavouriteRecipeViewModelFactory(favInteractor, belongsTo)
            )[FavouriteRecipeViewModel::class.java]

            favViewModel.loadFavouriteRecipes(belongsTo)

            favAdapter = FavouriteRecipeAdapter(emptyList())
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(this@FavouriteRecipeActivity)
                adapter = favAdapter
            }

            favViewModel.favouriteRecipes.observe(this@FavouriteRecipeActivity) { recipes ->
                recipes?.let {
                    favAdapter.updateData(it)
                }
            }
        }

        val backBtn: ImageView = findViewById(R.id.backBtnFav)
        backBtn.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val belongsTo = interactor.getAccountInfo().username!!

            favViewModel = ViewModelProvider(
                this@FavouriteRecipeActivity,
                FavouriteRecipeViewModelFactory(favInteractor, belongsTo)
            )[FavouriteRecipeViewModel::class.java]

            favViewModel.loadFavouriteRecipes(belongsTo)
        }
    }
}