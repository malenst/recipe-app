package com.tiodev.vegtummy.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiodev.vegtummy.domain.Category
import com.tiodev.vegtummy.domain.Recipe
import com.tiodev.vegtummy.domain.RecipeRepository

class CatRecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()

    val categories: LiveData<List<Category>>
        get() = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        recipeRepository.getCategories { categories -> _categories.postValue(categories) }
    }
}