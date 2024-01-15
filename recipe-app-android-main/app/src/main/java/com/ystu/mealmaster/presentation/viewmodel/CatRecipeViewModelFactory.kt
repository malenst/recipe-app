package com.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ystu.mealmaster.domain.interactor.RecipeInteractor

class CatRecipeViewModelFactory(private val interactor: RecipeInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatRecipeViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}