package ru.ystu.mealmaster.domain

enum class RecipeCategory(val value: String) {
    APPETIZERS("Закуски"),
    SOUPS("Супы"),
    MAIN_DISHES("Основное"),
    SIDE_DISHES("Гарниры"),
    DESSERTS("Десерты"),
    BAKERY("Выпечка"),
    BREAKFASTS("Завтраки"),
    BEVERAGES("Напитки"),
    SNACKS("Снеки"),
    SALADS("Салаты")
}