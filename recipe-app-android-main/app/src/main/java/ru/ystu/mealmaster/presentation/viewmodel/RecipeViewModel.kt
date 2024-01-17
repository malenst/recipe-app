package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class RecipeViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val recipeList = recipeInteractor.getRecipes()
                _recipes.postValue(recipeList)
            } catch (e: Exception) {
                _recipes.postValue(emptyList())
            }
        }
    }
}
