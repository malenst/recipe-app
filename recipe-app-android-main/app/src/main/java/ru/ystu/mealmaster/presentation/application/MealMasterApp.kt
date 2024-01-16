package ru.ystu.mealmaster.presentation.application

import android.app.Application
import ru.ystu.mealmaster.data.RecipeApiService


class MealMasterApp : Application() {
    companion object {
        lateinit var instance: MealMasterApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RecipeApiService.init(this)
    }
}