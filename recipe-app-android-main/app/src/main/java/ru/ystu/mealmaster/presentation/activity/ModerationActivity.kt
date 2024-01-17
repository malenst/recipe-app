package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.ApiResponseDto
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityMainBinding
import ru.ystu.mealmaster.databinding.ActivityModerationBinding
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.presentation.adapter.ModerationAdapter
import ru.ystu.mealmaster.presentation.adapter.RecipesByCategoryAdapter
import ru.ystu.mealmaster.presentation.viewmodel.ModerationViewModel
import ru.ystu.mealmaster.presentation.viewmodel.ModerationViewModelFactory
import ru.ystu.mealmaster.presentation.viewmodel.RecipesByCategoryViewModel
import ru.ystu.mealmaster.presentation.viewmodel.UncheckedRecipeViewModel
import ru.ystu.mealmaster.presentation.viewmodel.UncheckedRecipeViewModelFactory

class ModerationActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private lateinit var binding: ActivityModerationBinding
    private lateinit var moderationAdapter: ModerationAdapter
    private lateinit var moderationViewModel: ModerationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moderation)

        binding = ActivityModerationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.moderationRecview)

        RecipeApi.init(this)
        val api = ru.ystu.mealmaster.data.RecipeApi.api
        val repository = RecipeRepositoryImpl(api)
        val interactor = RecipeInteractorImpl(repository)

        moderationAdapter = ModerationAdapter(emptyList())

        moderationViewModel = ViewModelProvider(
            this,
            ModerationViewModelFactory(interactor)
        )[ModerationViewModel::class.java]

        moderationViewModel = ViewModelProvider(
            this,
            ModerationViewModelFactory(interactor)
        )[ModerationViewModel::class.java]

        binding.moderationRecview.apply {
            layoutManager = LinearLayoutManager(this@ModerationActivity)
            adapter = moderationAdapter
        }
//
        moderationViewModel.recipesByCategory.observe(this@ModerationActivity) { recipes ->
            recipes?.let {
                moderationAdapter.updateData(it)
            }
        }

//        api.getUncheckedRecipes().enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
//            override fun onResponse(
//                call: Call<ApiResponseDto<List<Recipe>>>,
//                response: Response<ApiResponseDto<List<Recipe>>>
//            ) {
//                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
//                    Log.d("MANGO", "MANGO")
//                } else {
//                    Log.d("MANGO", "MANGO")
//                 }
//            }
//
//            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
//                Log.d("KLUKVA", "KLUKVA")
//             }
//        })


        val backBtn: ImageView = findViewById(R.id.backBtnMain)
        // Exit activity
        backBtn.setOnClickListener { finish() }
    }
}