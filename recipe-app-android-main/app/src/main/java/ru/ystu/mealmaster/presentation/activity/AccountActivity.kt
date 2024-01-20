package ru.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.RecipeRepository
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl

class AccountActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var role: TextView
    private lateinit var logoutBtn: LinearLayout

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        backBtn = findViewById(R.id.back_btn_account)
        backBtn.setOnClickListener {
            finish()
        }

        username = findViewById(R.id.myAccountUsername)
        email = findViewById(R.id.myAccountEmail)
        role = findViewById(R.id.myAccountRole)

        logoutBtn = findViewById(R.id.sheet_logoutBtn)
        logoutBtn.setOnClickListener {
            lifecycleScope.launch {
                interactor.logout()
                val intent = Intent(this@AccountActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        lifecycleScope.launch {
            val user = interactor.getAccountInfo()
            Log.d("USERNAME", user.username)
            username.text = user.username
            Log.d("EMAIL", user.email)
            email.text = user.email
            Log.d("ROLE", user.role)
            role.text = user.role
        }
    }
}