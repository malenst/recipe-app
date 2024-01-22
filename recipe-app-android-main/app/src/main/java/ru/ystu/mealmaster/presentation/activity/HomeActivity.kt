package ru.ystu.mealmaster.presentation.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.databinding.ActivityHomeBinding
import ru.ystu.mealmaster.domain.User
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.adapter.CatRecipeAdapter
import ru.ystu.mealmaster.presentation.adapter.PopularRecipeAdapter
import ru.ystu.mealmaster.presentation.adapter.RecipeAdapter
import ru.ystu.mealmaster.presentation.viewmodel.*

class HomeActivity : AppCompatActivity() {
    private var recyclerViewHome: RecyclerView? = null
    private var popRecyclerViewHome: RecyclerView? = null
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var popularRecipeAdapter: PopularRecipeAdapter
    private lateinit var catRecipeAdapter: CatRecipeAdapter
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var currentUserRoleViewModel: CurrentUserRoleViewModel
    private lateinit var accountInfoViewModel: AccountInfoViewModel
    private lateinit var popularRecipeViewModel: PopularRecipeViewModel
    private lateinit var catRecipeViewModel: CatRecipeViewModel
    private lateinit var profileButton: ImageView
    private lateinit var menu: ImageView
    private lateinit var logo: ImageView
    private lateinit var nameLogo: ImageView
    private lateinit var fab: Button
    private lateinit var binding: ActivityHomeBinding

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private var lottie: LottieAnimationView? = null
    private var editText: EditText? = null
    private lateinit var icon: Icon
    private lateinit var userRole: String
    private lateinit var userEmail: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        icon = Icon.createWithResource(this@HomeActivity, R.drawable.moderation)

        currentUserRoleViewModel = ViewModelProvider(
            this,
            CurrentUserRoleViewModelFactory(interactor)
        )[CurrentUserRoleViewModel::class.java]

        accountInfoViewModel = ViewModelProvider(
            this,
            AccountInfoViewModelFactory(interactor)
        )[AccountInfoViewModel::class.java]

        checkPermissions()

        recyclerViewHome = findViewById(R.id.recview)
        popRecyclerViewHome = findViewById(R.id.rcview_popular)
        lottie = findViewById(R.id.lottie)
        editText = findViewById(R.id.editTextSearch)

        // Set all recipes
        setAllRecipesList()
        setPopularRecipesList()
        setCategoriesList()

        profileButton = findViewById(R.id.imageView4)
        profileButton.setOnClickListener {
            showBottomSheet() }

        menu = findViewById(R.id.menu)
//        menu.setOnClickListener {
//            val intent = Intent(this@HomeActivity, ModerationActivity::class.java)
//            startActivity(intent)
//        }

        nameLogo = findViewById(R.id.logo)
        logo = findViewById(R.id.bananas_icon)

        // Open search activity
        editText!!.setOnClickListener {
            val intent = Intent(this@HomeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        fab = findViewById(R.id.recipeBtn_add)
        fab.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun reloadData() {
        recipeViewModel.loadRecipes()
        checkPermissions()
    }

    private fun getCurrentUserRoleLiveData(): LiveData<String> {
        currentUserRoleViewModel.loadCurrentUser()
        return currentUserRoleViewModel.currentUserRole
    }

    private fun getAccountInfoLiveData(): LiveData<User> {
        accountInfoViewModel.loadAccountInfo()
        return accountInfoViewModel.accountInfo
    }

    private fun checkPermissions() {
        // Get user role
        getCurrentUserRoleLiveData().observe(this) { currentUserRole ->
            Log.d("CurrentUserRole", currentUserRole ?: "Role not found")
            userRole = currentUserRole.toString()
            if (currentUserRole == "MODERATOR" || currentUserRole == "ADMIN") {
                Log.d("UserIsModeratorOrAdmin", true.toString())
                logo.setImageIcon(icon)
                logo.setOnClickListener {
                    val intent = Intent(this@HomeActivity, ModerationActivity::class.java)
                    startActivity(intent)
                }
            } else if (currentUserRole == "USER") {
                Log.d("UserIsUser", true.toString())
                Log.d("MENU", menu.toString())

                profileButton.setOnClickListener {
                    showBottomSheet() }
            }
            if (currentUserRole == "ANONYMOUS") {
                profileButton.setOnClickListener {
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setAllRecipesList() {
        setContentView(binding.root)

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
        popularRecipeAdapter = PopularRecipeAdapter(emptyList())
        popularRecipeViewModel = ViewModelProvider(
            this,
            PopularRecipeViewModelFactory(interactor)
        )[PopularRecipeViewModel::class.java]
        binding.rcviewPopular.apply {
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularRecipeAdapter
        }

        popularRecipeViewModel.popRecipes.observe(this@HomeActivity) { recipes ->
            recipes?.let {
                popularRecipeAdapter.updateData(it)
            }
        }


    }
    private fun showBottomSheet() {
        Log.d("PERETZ", "SHIT")
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)
        val myRecipes = dialog.findViewById<LinearLayout>(R.id.myRecepiesBtn)
        val userName = dialog.findViewById<LinearLayout>(R.id.myUsername)
        val userNameText = dialog.findViewById<TextView>(R.id.myAccountUsername)
        val logout = dialog.findViewById<LinearLayout>(R.id.sheet_logoutBtn)

        getAccountInfoLiveData().observe(this) { account ->
            userNameText.text = account.username

        }

        myRecipes.setOnClickListener {
            val intent = Intent(this@HomeActivity, MyRecipesActivity::class.java)
            startActivity(intent)
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(getString(R.string.privacy_policy_url))
//            startActivity(intent)
        }
        userName.setOnClickListener {
            val intent = Intent(this@HomeActivity, AccountActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            lifecycleScope.launch {
                interactor.logout()
                val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
    // Start MainActivity(Recipe list) with intent message
    @Suppress("unused")
    private fun start(p: String?, tittle: String?) {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        intent.putExtra("Category", p)
        intent.putExtra("tittle", tittle)
        startActivity(intent)
    }
}