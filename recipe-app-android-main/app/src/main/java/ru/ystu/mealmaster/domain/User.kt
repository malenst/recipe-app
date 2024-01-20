package ru.ystu.mealmaster.domain

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    @SerializedName("join_date_time")
    val joinDateTime: String
)
