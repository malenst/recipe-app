package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.domain.interactor.FavouriteRecipeInteractor

class FavouriteRecipeViewModel(private val interactor: FavouriteRecipeInteractor, belongsTo: String) : ViewModel() {

    private val _favouriteRecipes = MutableLiveData<List<FavouriteRecipe>>()
    val favouriteRecipes: LiveData<List<FavouriteRecipe>>
        get() = _favouriteRecipes

    init {
        loadFavouriteRecipes(belongsTo)
    }

   fun loadFavouriteRecipes(belongsTo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipesList = interactor.getAllFavouriteRecipes(belongsTo)
                _favouriteRecipes.postValue(recipesList)
            } catch (e: Exception) {
                _favouriteRecipes.postValue(emptyList())
            }
        }
    }
}