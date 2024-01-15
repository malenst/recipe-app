package ru.ystu.mealmaster.domain

import com.google.gson.annotations.SerializedName

data class Review (
    val author: String,
    val text: String,
    val rating: Int,
    @SerializedName("timestamp")
    val date: String
)