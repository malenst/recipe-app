package ru.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import ru.ystu.mealmaster.presentation.viewmodel.AccountInfoViewModel
import ru.ystu.mealmaster.presentation.viewmodel.AccountInfoViewModelFactory

class AccountActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var username: TextView
//    private lateinit var email: TextView
//    private lateinit var role: TextView
    private lateinit var favBtn: LinearLayout
    private lateinit var logoutBtn: LinearLayout
    private lateinit var accountInfoViewModel: AccountInfoViewModel

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        setContentView(R.layout.activity_account)
        interactor = RecipeInteractorImpl(repository)
        accountInfoViewModel = ViewModelProvider(
            this,
            AccountInfoViewModelFactory(interactor)
        )[AccountInfoViewModel::class.java]

        favBtn = findViewById(R.id.favRecipesBtn)
        favBtn.setOnClickListener {
            val intent = Intent(this@AccountActivity, FavouriteRecipeActivity::class.java)
            startActivity(intent)
        }

        backBtn = findViewById(R.id.back_btn_account)
        backBtn.setOnClickListener {
            finish()
        }

        username = findViewById(R.id.myAccountUsername)
//        email = findViewById(R.id.myAccountEmail)
//        role = findViewById(R.id.myAccountRole)

        logoutBtn = findViewById(R.id.sheet_logoutBtn)
        logoutBtn.setOnClickListener {
            lifecycleScope.launch {
                interactor.logout()
                val intent = Intent(this@AccountActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        accountInfoViewModel.loadAccountInfo()
            accountInfoViewModel.accountInfo.observe(this) { account ->
            username.text = account.username
        }
    }
}