package ru.ystu.mealmaster.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.domain.User
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor

class AccountInfoViewModel(private val recipeInteractor: RecipeInteractor) : ViewModel() {

    private val _accountInfo = MutableLiveData<User>()
    val accountInfo: LiveData<User> get() = _accountInfo

    init {
        loadAccountInfo()
    }

    fun loadAccountInfo() {
        viewModelScope.launch {
            try {
                val accountInfo = recipeInteractor.getAccountInfo()
                _accountInfo.postValue(accountInfo)
            } catch (ignored: JsonSyntaxException) {
            } catch (e: Exception) {
                Log.e("Exception", e.stackTraceToString())
            }
        }
    }
}
