package ru.ystu.mealmaster.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import kotlinx.coroutines.launch

class ModerationViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _recipesByCategory = MutableLiveData<List<Recipe>>()
    val recipesByCategory: LiveData<List<Recipe>> get() = _recipesByCategory

    init {
        loadUncheckedRecipes()
    }

    private fun loadUncheckedRecipes() {
        viewModelScope.launch {
            try {
                val recipesList = recipeInteractor.getUncheckedRecipes()
                Log.d("YABLOKO", recipesList.toString())
                _recipesByCategory.postValue(recipesList)
            } catch (e: Exception) {
                _recipesByCategory.postValue(emptyList())
            }
        }
    }
}