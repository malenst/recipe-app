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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.ApiResponseDto
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.domain.Recipe

class LoginActivity : AppCompatActivity() {

    private lateinit var api: RecipeApi
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        RecipeApiService.init(this)
        api = RecipeApiService.api

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
        api.login(username, password).enqueue(object : Callback<ApiResponseDto<List<Recipe>>> {
            override fun onResponse(
                call: Call<ApiResponseDto<List<Recipe>>>,
                response: Response<ApiResponseDto<List<Recipe>>>
            ) {
                if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
                    Log.d("COOK", response.headers().values("Set-Cookie").toString())
                    saveCookies((response.headers().values("Set-Cookie")))

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponseDto<List<Recipe>>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })
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
