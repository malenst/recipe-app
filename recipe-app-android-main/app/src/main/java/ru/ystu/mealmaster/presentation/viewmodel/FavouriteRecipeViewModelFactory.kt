package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractor

class FavouriteRecipeViewModelFactory(private val interactor: FavouriteRecipeInteractor, private val belongsTo: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteRecipeViewModel(interactor, belongsTo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}