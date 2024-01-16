package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import kotlinx.coroutines.launch

class UncheckedRecipeViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            try {
                val recipeList = recipeInteractor.getUncheckedRecipes()
                _recipes.postValue(recipeList)
            } catch (e: Exception) {
                _recipes.postValue(emptyList())
            }
        }
    }
}
