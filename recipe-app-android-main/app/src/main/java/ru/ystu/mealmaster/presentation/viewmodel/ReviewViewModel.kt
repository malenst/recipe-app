package ru.ystu.mealmaster.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.dto.ReviewDTO
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import java.util.*

class ReviewViewModel(private val recipeInteractor: RecipeInteractor, private val id: UUID) : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewDTO>>()
    val reviews: LiveData<List<ReviewDTO>> get() = _reviews

    init {
        loadReviewsByCategory()
    }

    private fun loadReviewsByCategory() {
        viewModelScope.launch {
            try {
                val reviewsList = recipeInteractor.getReviewsById(id)
                _reviews.postValue(reviewsList)
            } catch (e: Exception) {
                _reviews.postValue(emptyList())
            }
        }
    }
}