package ru.ystu.mealmaster.util.interceptor

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddCookiesInterceptor(context: Context) : Interceptor {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val preferences = sharedPreferences.getStringSet("remember-me", HashSet())

        for (cookie in preferences!!) {
            builder.addHeader("Cookie", cookie)
            Log.v("OkHttp", "Adding Header: $cookie")
        }

        return chain.proceed(builder.build())
    }
}

