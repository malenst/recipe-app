package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import kotlinx.coroutines.launch
import java.util.UUID

class MyRecipesViewModel(private val recipeInteractor: RecipeInteractor, private val username: String) : ViewModel() {

    private val _recipesByUser = MutableLiveData<List<Recipe>>()
    val recipesByUser: LiveData<List<Recipe>> get() = _recipesByUser

    init {
        loadRecipesByUser()
    }

    private fun loadRecipesByUser() {
        viewModelScope.launch {
            try {
                val recipesList = recipeInteractor.getRecipesByUser(username)
                _recipesByUser.postValue(recipesList)
            } catch (e: Exception) {
                _recipesByUser.postValue(emptyList())
            }
        }
    }
}