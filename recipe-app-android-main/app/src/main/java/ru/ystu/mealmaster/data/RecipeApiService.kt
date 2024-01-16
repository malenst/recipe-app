package ru.ystu.mealmaster.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.ystu.mealmaster.presentation.application.MealMasterApp
import ru.ystu.mealmaster.util.interceptor.AddCookiesInterceptor
import ru.ystu.mealmaster.util.interceptor.ReceivedCookiesInterceptor
import ru.ystu.mealmaster.util.persistent.CustomPersistentCookieJar
import java.net.CookieHandler
import java.net.CookieManager
import java.util.concurrent.TimeUnit


object RecipeApiService {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private lateinit var retrofit: Retrofit
    lateinit var api: RecipeApi
    private lateinit var cookieJar: CookieJar

    private lateinit var addCookiesInterceptor: AddCookiesInterceptor
    private lateinit var receivedCookiesInterceptor: ReceivedCookiesInterceptor

    fun init(context: Context) {
        val appContext = context.applicationContext
        cookieJar = CustomPersistentCookieJar(appContext)
        addCookiesInterceptor = AddCookiesInterceptor(appContext)
        receivedCookiesInterceptor = ReceivedCookiesInterceptor(appContext)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(RecipeApi::class.java)

        loadCookies()
    }

    private fun loadCookies() {
        val sharedPreferences = MealMasterApp.instance.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val cookiesString = sharedPreferences.getString("cookies", null)
        cookiesString?.let {
            val cookies = it.split(";").mapNotNull { cookieString ->
                Log.d("Cookies", "Loading cookie: $cookieString")
                Cookie.parse(BASE_URL.toHttpUrl(), cookieString)
            }
            cookieJar.loadInitialCookies(cookies)
        }
    }

    private fun CookieJar.loadInitialCookies(cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            saveFromResponse(BASE_URL.toHttpUrl(), listOf(cookie))
        }
    }

    private val client: OkHttpClient by lazy {
        val cookieHandler: CookieHandler = CookieManager()

        OkHttpClient.Builder()
            .followRedirects(false)
            .cookieJar(JavaNetCookieJar(cookieHandler))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request()
                val cookies = request.headers
                Log.d("HTTP", "Cookies from request: $cookies")

                chain.proceed(request)
            }
            .addInterceptor(addCookiesInterceptor)
            .addInterceptor(receivedCookiesInterceptor)
            .build()
    }

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
}