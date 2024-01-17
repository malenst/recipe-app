package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class ModerationViewModelFactory(private val interactor: RecipeInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModerationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModerationViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}