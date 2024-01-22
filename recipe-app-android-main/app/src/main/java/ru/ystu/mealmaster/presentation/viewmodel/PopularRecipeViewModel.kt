package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class PopularRecipeViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _popRecipes = MutableLiveData<List<Recipe>>()
    val popRecipes: LiveData<List<Recipe>> get() = _popRecipes

    init {
        loadPopularRecipes()
    }

    fun loadPopularRecipes() {
        viewModelScope.launch {
            try {
                val popularRecipesList = recipeInteractor.getTop10Recipes()
                _popRecipes.postValue(popularRecipesList)
            } catch (e: Exception) {
                _popRecipes.postValue(emptyList())
            }
        }
    }
}