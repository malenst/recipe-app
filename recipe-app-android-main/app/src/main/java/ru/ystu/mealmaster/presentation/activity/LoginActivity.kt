package ru.ystu.mealmaster.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

class LoginActivity : AppCompatActivity() {

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            login(username, password)
        }

        backBtn = findViewById(R.id.back_btn_login)
        backBtn.setOnClickListener { finish() }
    }

    private fun login(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val (recipes, cookies) = interactor.login(username, password)
                if (cookies != null) {
                    saveCookies(cookies)
                }
                Log.d("RecipesAfterSuccessfulLogin", recipes.toString())
                intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Login failed: invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCookies(cookies: List<String>) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val cookiesString = cookies.joinToString(";")

        editor.putString("cookies", cookiesString)
        Log.d("Cookies", "Saving cookies: $cookiesString")
        editor.apply()
    }
}
