package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class ReciewViewModelFactory(private val interactor: RecipeInteractor, private val category: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReciewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReciewViewModel(interactor, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}