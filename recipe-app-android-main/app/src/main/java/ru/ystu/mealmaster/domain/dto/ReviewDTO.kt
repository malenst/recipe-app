package ru.ystu.mealmaster.domain.dto

import com.google.gson.annotations.SerializedName

data class ReviewDTO (
    val author: String,
    val text: String,
    val rating: Int,
    @SerializedName("timestamp")
    val date: String
)