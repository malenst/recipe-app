package ru.ystu.mealmaster.domain

data class RegistrationRequestDTO(
    val username: String,
    val email: String,
    val password: String
)
