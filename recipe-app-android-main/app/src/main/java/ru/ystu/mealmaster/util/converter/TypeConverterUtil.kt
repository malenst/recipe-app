package ru.ystu.mealmaster.util.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ystu.mealmaster.domain.Review

class TypeConverterUtil {
    @TypeConverter
    fun mapToString(value: Map<String, String>?): String = Gson().toJson(value)

    @TypeConverter
    fun stringToMap(value: String): Map<String, String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun reviewsListToString(value: List<Review>?): String = Gson().toJson(value)

    @TypeConverter
    fun stringToReviewsList(value: String): List<Review>? {
        val type = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson(value, type)
    }
}
