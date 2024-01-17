package ru.ystu.mealmaster.util.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ru.ystu.mealmaster.util.sharedpref.SharedPrefManager
import java.io.IOException

class AddCookiesInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val sharedPrefManager = SharedPrefManager(context)
        sharedPrefManager.getSessionId()?.let { sessionId ->
            builder.addHeader("Cookie", "JSESSIONID=$sessionId")
        }

        return chain.proceed(builder.build())
    }
}

