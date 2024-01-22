package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivitySearchBinding
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.RecipeAdapter
import ru.ystu.mealmaster.presentation.viewmodel.PopularRecipeViewModel
import ru.ystu.mealmaster.presentation.viewmodel.PopularRecipeViewModelFactory

class SearchActivity : AppCompatActivity() {
    private var search: EditText? = null
    private var backBtn: ImageView? = null
    private var recyclerview: RecyclerView? = null
    private var popularRecipeAdapter: RecipeAdapter? = null
    private var popularRecipeViewModel: PopularRecipeViewModel? = null
    private var binding: ActivitySearchBinding? = null

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    //    List<User> dataPopular = new ArrayList<>();
    //    SearchAdapter adapter;
    //    List<User> recipes;
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
            PopularRecipeViewModelFactory(interactor)
        )[PopularRecipeViewModel::class.java]
        binding?.rcview?.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = popularRecipeAdapter
        }

        popularRecipeViewModel!!.loadPopularRecipes()

        popularRecipeViewModel!!.popRecipes.observe(this@SearchActivity) { recipes ->
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


        // Set adapter to search recyclerView
//        adapter = new SearchAdapter(dataPopular, getApplicationContext());
//        rcview.setAdapter(adapter);


        // Search from all recipes when Edittext data changed
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Method required*
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Method required*
//            }
//
////            @Override
////            public void afterTextChanged(Editable s) {
////
////                if(!s.toString().equals("")){ // Search if new alphabet added
////                    filter(s.toString());
////                }
////
////
////            }
//        });


        // Exit activity
        backBtn?.setOnClickListener { v: View? ->
            imm.hideSoftInputFromWindow(search?.windowToken, 0)
            finish()
        }
    } // Filter the searched item from all recipes
    //    public void filter(String text) {
    //        List<User> filterList = new ArrayList<>();
    //
    //        for(int i = 0; i<recipes.size(); i++){ // Loop for check searched item in recipe list
    //            if(recipes.get(i).getTittle().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
    //                filterList.add(recipes.get(i));
    //            }
    //        }
    //
    //        // Update search recyclerView with new item
    //        adapter.filterList(filterList);
    //
    //    }
}