package com.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ystu.mealmaster.domain.interactor.RecipeInteractor

class RecipesByCategoryViewModelFactory(private val interactor: RecipeInteractor, private val category: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesByCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipesByCategoryViewModel(interactor, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}