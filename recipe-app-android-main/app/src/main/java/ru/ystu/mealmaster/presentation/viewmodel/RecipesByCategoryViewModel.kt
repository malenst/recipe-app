package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import kotlinx.coroutines.launch

class RecipesByCategoryViewModel(private val recipeInteractor: RecipeInteractor, private val category: String) : ViewModel() {

    private val _recipesByCategory = MutableLiveData<List<RecipeDTO>>()
    val recipesByCategory: LiveData<List<RecipeDTO>> get() = _recipesByCategory

    init {
        loadRecipesByCategory()
    }

    private fun loadRecipesByCategory() {
        viewModelScope.launch {
            try {
                val recipesList = recipeInteractor.getRecipesByCategory(category)
                _recipesByCategory.postValue(recipesList)
            } catch (e: Exception) {
                _recipesByCategory.postValue(emptyList())
            }
        }
    }
}