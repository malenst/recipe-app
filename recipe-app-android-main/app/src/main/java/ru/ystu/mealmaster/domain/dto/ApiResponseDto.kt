package ru.ystu.mealmaster.domain.dto

data class ApiResponseDto<T>(
    val type: String,
    val status: String,
    val response: T,
    val cause: Throwable
)
