package ru.ystu.mealmaster

import android.app.Application
import ru.ystu.mealmaster.data.RecipeApi


class MealMasterApp : Application() {
    companion object {
        lateinit var instance: MealMasterApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RecipeApi.init(this)
    }
}