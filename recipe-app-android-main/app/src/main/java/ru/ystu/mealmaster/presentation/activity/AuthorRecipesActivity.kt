package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivitySearchBinding
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.presentation.adapter.MyRecipesAdapter
import ru.ystu.mealmaster.presentation.viewmodel.MyRecipesViewModel
import ru.ystu.mealmaster.presentation.viewmodel.MyRecipesViewModelFactory

class AuthorRecipesActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private lateinit var activityAuthorRecipesTittleTextView: TextView
    private lateinit var binding: ActivitySearchBinding
    private lateinit var authorRecipesAdapter: MyRecipesAdapter
    private lateinit var authorRecipesViewModel: MyRecipesViewModel
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activityAuthorRecipesTittleTextView = findViewById(R.id.activitySearchTittle)
        recyclerView = findViewById(R.id.rcview)

        RecipeApi.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApi.api
        val repository = RecipeRepositoryImpl(api, this)
        val interactor = RecipeInteractorImpl(repository)

        authorRecipesAdapter = MyRecipesAdapter(emptyList())

        val authorUsername = intent.getStringExtra("AUTHOR_USERNAME")
        if (authorUsername != null) {
            username = authorUsername
//            activityAuthorRecipesTittleTextView.text = getString(R.string.author_recipes_text) + " $username"
        }

        authorRecipesViewModel = ViewModelProvider(
            this,
            MyRecipesViewModelFactory(interactor, username, true)
        )[MyRecipesViewModel::class.java]


        authorRecipesViewModel.recipesByUser.observe(this@AuthorRecipesActivity) { recipes ->
            recipes?.let {
                authorRecipesAdapter.updateData(it)
            }
        }

        binding.rcview.apply {
            layoutManager = LinearLayoutManager(this@AuthorRecipesActivity)
            adapter = authorRecipesAdapter
        }

        val backBtn: ImageView = findViewById(R.id.back_to_home)
        // Exit activity
        backBtn.setOnClickListener { finish() }
    }
}