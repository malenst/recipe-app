package ru.ystu.mealmaster.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.domain.Recipe

object RecipeUtils {
    fun formatRecipeInfo(recipe: Recipe, txt: TextView?, views: TextView?, reviews: TextView?, img: ImageView?) {
        txt?.text = recipe.name
        Log.d("RECIPE VIEWS", recipe.views.toString())
        views?.text = recipe.views.toString()

        val reviewsText = recipe.reviews?.joinToString(separator = "\n\n") { review ->
            "Автор: ${review.author}\nОтзыв: ${review.text}\nОценка: ${review.rating}\nВремя: ${review.date}"
        }
        if (reviewsText!!.isNotEmpty()) {
            reviews?.text = reviewsText
        }

        if (!recipe.image.isNullOrEmpty()) {
            val imageNorm = recipe.image
                .replace("http", BuildConfig.BASE_PROTOCOL)
                .replace("localhost", BuildConfig.BASE_HOST)
                .replace("8080", BuildConfig.BASE_PORT)
            Picasso.get().load(imageNorm).into(img)
            img?.visibility = View.VISIBLE
        }
    }
}
