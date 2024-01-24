package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivitySearchBinding
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.RecipeAdapter
import ru.ystu.mealmaster.presentation.viewmodel.RecipeViewModel
import ru.ystu.mealmaster.presentation.viewmodel.RecipeViewModelFactory
import java.util.*

class SearchActivity : AppCompatActivity() {
    private var search: EditText? = null
    private var backBtn: ImageView? = null
    private var recyclerview: RecyclerView? = null
    private var popularRecipeAdapter: RecipeAdapter? = null
    private var popularRecipeViewModel: RecipeViewModel? = null
    private var binding: ActivitySearchBinding? = null

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private lateinit var recipeDTOS: List<RecipeDTO>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        // Find views
        search = findViewById(R.id.search)
        backBtn = findViewById(R.id.back_to_home)
        recyclerview = findViewById(R.id.rcview)

        // Show and focus the keyboard
        search?.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        popularRecipeAdapter = RecipeAdapter(emptyList())
        popularRecipeViewModel = ViewModelProvider(
            this,
            RecipeViewModelFactory(interactor)
        )[RecipeViewModel::class.java]
        binding?.rcview?.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = popularRecipeAdapter
        }

        popularRecipeViewModel!!.loadRecipes()

        popularRecipeViewModel!!.recipes.observe(this@SearchActivity) { recipes ->
            recipes?.let {
                Log.d("VVVVVV", recipes.toString())
                popularRecipeAdapter!!.updateData(it)
            }
        }

        // Hide keyboard when recyclerView item clicked
        recyclerview?.setOnTouchListener { v: View?, event: MotionEvent? ->
            imm.hideSoftInputFromWindow(search?.windowToken, 0)
            false
        }

        lifecycleScope.launch {
            recipeDTOS = interactor.getRecipes()
        }

        search?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

        backBtn?.setOnClickListener {
            imm.hideSoftInputFromWindow(search?.windowToken, 0)
            finish()
        }
    }

    // Функция фильтрации данных
    fun filter(text: String) {
        val filteredList = ArrayList<RecipeDTO>()

        for (recipe in recipeDTOS) {
            if (recipe.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(recipe)
            }
        }

        popularRecipeAdapter?.updateData(filteredList)
    }
}