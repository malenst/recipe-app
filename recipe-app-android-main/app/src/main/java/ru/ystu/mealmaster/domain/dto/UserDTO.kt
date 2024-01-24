package ru.ystu.mealmaster.domain.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    val id: String?,
    val username: String?,
    val email: String?,
    val role: String?,
    @SerializedName("join_date_time")
    val joinDateTime: String?
)
