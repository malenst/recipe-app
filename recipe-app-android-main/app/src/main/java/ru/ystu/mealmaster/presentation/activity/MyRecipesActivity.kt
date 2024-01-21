package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
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
import ru.ystu.mealmaster.presentation.viewmodel.AccountInfoViewModel
import ru.ystu.mealmaster.presentation.viewmodel.AccountInfoViewModelFactory
import ru.ystu.mealmaster.presentation.viewmodel.MyRecipesViewModel
import ru.ystu.mealmaster.presentation.viewmodel.MyRecipesViewModelFactory

class MyRecipesActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private lateinit var binding: ActivitySearchBinding
    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private lateinit var myRecipesViewModel: MyRecipesViewModel
    private lateinit var accountInfoViewModel: AccountInfoViewModel
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.rcview)

        RecipeApi.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApi.api
        val repository = RecipeRepositoryImpl(api, this)
        val interactor = RecipeInteractorImpl(repository)

        accountInfoViewModel = ViewModelProvider(
            this,
            AccountInfoViewModelFactory(interactor)
        )[AccountInfoViewModel::class.java]

        myRecipesAdapter = MyRecipesAdapter(emptyList())

        accountInfoViewModel.loadAccountInfo()
        accountInfoViewModel.accountInfo.observe(this) { account ->
            username = account.username!!
            myRecipesViewModel = ViewModelProvider(
                this,
                MyRecipesViewModelFactory(interactor, username)
            )[MyRecipesViewModel::class.java]


            myRecipesViewModel.recipesByUser.observe(this@MyRecipesActivity) { recipes ->
                recipes?.let {
                    myRecipesAdapter.updateData(it)
                }
            }
        }

        binding.rcview.apply {
            layoutManager = LinearLayoutManager(this@MyRecipesActivity)
            adapter = myRecipesAdapter
        }

        val backBtn: ImageView = findViewById(R.id.back_to_home)
        // Exit activity
        backBtn.setOnClickListener { finish() }
    }
}