package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ystu.mealmaster.domain.dto.CategoryDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import kotlinx.coroutines.launch

class CatRecipeViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryDTO>>()
    val categories: LiveData<List<CategoryDTO>> get() = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categoryList = recipeInteractor.getCategories()
                _categories.postValue(categoryList)
            } catch (e: Exception) {
                _categories.postValue(emptyList())
            }
        }
    }
}
