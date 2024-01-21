package ru.ystu.mealmaster.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.util.converter.TypeConverterUtil

@TypeConverters(TypeConverterUtil::class)
@Database(entities = [FavouriteRecipe::class], version = 3, exportSchema = false)
abstract class FavouriteRecipeDatabase : RoomDatabase() {
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao

    companion object {
        private const val DB_NAME = "favourite_recipe"
        private var instance: FavouriteRecipeDatabase? = null

        fun getInstance(application: Application?): FavouriteRecipeDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    application!!,
                    FavouriteRecipeDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
