package ru.ystu.mealmaster.util.interceptor

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor(context: Context) : Interceptor {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = sharedPreferences.getStringSet("remember-me", HashSet())?.let { HashSet(it) }

            cookies?.addAll(originalResponse.headers("Set-Cookie"))

            sharedPreferences.edit()
                .putStringSet("remember-me", cookies)
                .apply()
        }

        return originalResponse
    }
}

