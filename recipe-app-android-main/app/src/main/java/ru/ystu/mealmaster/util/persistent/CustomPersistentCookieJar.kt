package ru.ystu.mealmaster.util.persistent

import android.content.Context
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CustomPersistentCookieJar(context: Context) : CookieJar {
    private val cookiePersistor = SharedPrefsCookiePersistor(context)

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookiePersistor.loadAll()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookiePersistor.saveAll(cookies)
    }

    fun clear() {
        cookiePersistor.clear()
    }
}