package ru.ystu.mealmaster.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RecipeApiService {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    val cookieJar = object : CookieJar {
        private val cookieStore = HashMap<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: ArrayList()
        }
    }

    private var client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(ru.ystu.mealmaster.data.RecipeApiService.cookieJar)
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .apply {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            this.addInterceptor(interceptor)
        }
        .build()

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ru.ystu.mealmaster.data.RecipeApiService.BASE_URL)
        .client(ru.ystu.mealmaster.data.RecipeApiService.client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(ru.ystu.mealmaster.data.RecipeApiService.gson))
        .build()

    val api: ru.ystu.mealmaster.data.RecipeApi = ru.ystu.mealmaster.data.RecipeApiService.retrofit.create(ru.ystu.mealmaster.data.RecipeApi::class.java)

}