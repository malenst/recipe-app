package ru.ystu.mealmaster.data

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.util.interceptor.AddCookiesInterceptor
import ru.ystu.mealmaster.util.interceptor.ReceivedCookiesInterceptor
import ru.ystu.mealmaster.util.persistent.CustomPersistentCookieJar
import java.util.concurrent.TimeUnit


object RecipeApi {
    private lateinit var BASE_API_URL: String

    lateinit var client: OkHttpClient
    private lateinit var retrofit: Retrofit
    lateinit var api: RecipeApiService

    fun init(context: Context) {
        val appContext = context.applicationContext
        BASE_API_URL = "${BuildConfig.BASE_PROTOCOL}://${BuildConfig.BASE_HOST}:${BuildConfig.BASE_PORT}${BuildConfig.API_PATH}"

        client = OkHttpClient.Builder()
            //.followRedirects(false)
            .cookieJar(CustomPersistentCookieJar(appContext))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AddCookiesInterceptor(appContext))
            .addInterceptor(ReceivedCookiesInterceptor(appContext))
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(RecipeApiService::class.java)
    }
}
