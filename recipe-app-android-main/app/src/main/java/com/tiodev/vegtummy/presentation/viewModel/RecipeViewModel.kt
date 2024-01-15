package com.tiodev.vegtummy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tiodev.vegtummy.domain.Recipe
import com.tiodev.vegtummy.domain.RecipeRepository

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()

    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        recipeRepository.getRecipes { recipes -> _recipes.postValue(recipes) }
    }
}