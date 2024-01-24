package ru.ystu.mealmaster.domain.dto

data class RegistrationRequestDTO(
    val username: String,
    val email: String,
    val password: String
)
