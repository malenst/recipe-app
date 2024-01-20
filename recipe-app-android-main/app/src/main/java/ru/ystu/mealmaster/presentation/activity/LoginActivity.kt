package ru.ystu.mealmaster.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.RecipeRepository
import ru.ystu.mealmaster.domain.RegistrationRequestDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl

class LoginActivity : AppCompatActivity() {

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private lateinit var backBtn: ImageView
    private lateinit var registerTextButton: TextView
    private lateinit var loginTextButton: TextView

    private var loginConstraint: ConstraintLayout? = null
    private var registerConstraint: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtUsernameRegister = findViewById<EditText>(R.id.txtUsername_register)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val txtPasswordRegister = findViewById<EditText>(R.id.txtPassword_register)
        val txtEmailRegister = findViewById<EditText>(R.id.txtEmail_register)

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            login(username, password)
        }

        btnRegister.setOnClickListener {
            val username = txtUsernameRegister.text.toString()
            val email = txtEmailRegister.text.toString()
            val password = txtPasswordRegister.text.toString()

            register(username, email, password)
        }

        backBtn = findViewById(R.id.back_btn_login)
        backBtn.setOnClickListener { finish() }

        registerTextButton = findViewById(R.id.textView_register)
        loginTextButton = findViewById(R.id.textView_auth)

        registerConstraint = findViewById(R.id.constraintLayout_register)
        loginConstraint = findViewById(R.id.constraintLayout_auth)

        val colorBlack = resources.getColor(R.color.black)
        val colorBlack60 = resources.getColor(R.color.black60)

        registerTextButton.setOnClickListener {

            loginTextButton.setTextColor(colorBlack60)
            registerTextButton.setTextColor(colorBlack)
            registerConstraint?.visibility = View.VISIBLE
            loginConstraint?.visibility = View.GONE
        }

        loginTextButton.setOnClickListener {
            registerTextButton.setTextColor(colorBlack60)
            loginTextButton.setTextColor(colorBlack)
            registerConstraint?.visibility = View.GONE
            loginConstraint?.visibility = View.VISIBLE
        }

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

    private fun register(username: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                val user = interactor.register(RegistrationRequestDTO(username, email, password))
                Log.d("User registered", user.toString())
                intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Register failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
