package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import java.util.UUID

class MyRecipesViewModelFactory(private val interactor: RecipeInteractor, private val username: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyRecipesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyRecipesViewModel(interactor, username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}