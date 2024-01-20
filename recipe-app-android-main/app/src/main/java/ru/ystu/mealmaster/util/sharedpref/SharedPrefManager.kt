package ru.ystu.mealmaster.util.sharedpref

import android.content.Context
import android.content.SharedPreferences


class SharedPrefManager(
    context: Context
) {
    private var pref: SharedPreferences

    private var editor: SharedPreferences.Editor

    private var privateMode: Int = 0


    init {
        pref = context.getSharedPreferences(PREF_NAME, privateMode)
        editor = pref.edit()
    }

    fun saveSession(sessionId: String?) {
        editor.putString("SESSION_ID", sessionId)
        editor.commit()
    }

    fun getSessionId(): String? {
        val sessionId = pref.getString("SESSION_ID", null)
        return sessionId
    }


    companion object {
        private const val PREF_NAME = "AndroidHivePref"
    }

    fun clearSession() {
        pref.edit().remove("SESSION_ID").apply()
    }
}