package com.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ystu.mealmaster.domain.interactor.RecipeInteractor

class RecipeViewModelFactory(private val interactor: RecipeInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}