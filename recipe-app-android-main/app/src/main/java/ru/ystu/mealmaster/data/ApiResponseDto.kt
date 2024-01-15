package ru.ystu.mealmaster.data

data class ApiResponseDto<T>(val type: String, val status: String, val response: T)
