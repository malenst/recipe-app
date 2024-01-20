package ru.ystu.mealmaster.domain.enumeration

enum class ChangeType(val value: String) {
    CREATE("Создание"),
    UPDATE("Обновление"),
    DELETE("Удаление")
}