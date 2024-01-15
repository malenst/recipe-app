package com.tiodev.vegtummy.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiodev.vegtummy.domain.Recipe
import com.tiodev.vegtummy.domain.RecipeRepository

class PopRecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _popRecipes = MutableLiveData<List<Recipe>>()

    val popRecipes: LiveData<List<Recipe>>
        get() = _popRecipes

    init {
        loadPopularRecipes()
    }

    private fun loadPopularRecipes() {
        recipeRepository.getTop10Recipes { recipes -> _popRecipes.postValue(recipes) }
    }
}