package ru.ystu.mealmaster.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class ModerationViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _pendingRecipes = MutableLiveData<List<RecipeDTO>>()
    val pendingRecipes: LiveData<List<RecipeDTO>> get() = _pendingRecipes

    init {
        loadUncheckedRecipes()
    }

    private fun loadUncheckedRecipes() {
        viewModelScope.launch {
            try {
                val recipesList = recipeInteractor.getUncheckedRecipes()
                Log.d("YABLOKO", recipesList.toString())
                _pendingRecipes.postValue(recipesList)
            } catch (e: Exception) {
                _pendingRecipes.postValue(emptyList())
            }
        }
    }
}