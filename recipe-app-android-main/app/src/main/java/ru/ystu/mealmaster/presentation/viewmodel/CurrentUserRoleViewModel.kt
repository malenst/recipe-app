package ru.ystu.mealmaster.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class CurrentUserRoleViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _currentUserRole = MutableLiveData<String>()
    val currentUserRole: LiveData<String> get() = _currentUserRole

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val currentUserRole = recipeInteractor.getCurrentUserRole()
                _currentUserRole.postValue(currentUserRole)
            } catch (ignored: JsonSyntaxException) {
            } catch (e: Exception) {
                Log.e("Exception", e.stackTraceToString())
            }
        }
    }
}
