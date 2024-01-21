package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.presentation.activity.RecipeActivity

class FavouriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.favName)
    //private val avgRating = itemView.findViewById<TextView>(R.id.listRatingByCategory)
    private val cookingTimeTextView = itemView.findViewById<TextView>(R.id.favCookingTime)
    private val imageView = itemView.findViewById<ImageView>(R.id.favImg)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(recipe: FavouriteRecipe) {
        context = itemView.context

        itemView.setOnClickListener {
            intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.apiId.toString())
            context.startActivity(intent)
        }

        nameTextView.text = recipe.name
        cookingTimeTextView.text = recipe.cookingTime

        if (!recipe.image.isNullOrEmpty()) {
            val imageNorm = recipe.image
                .replace("http", BuildConfig.BASE_PROTOCOL)
                .replace("localhost", BuildConfig.BASE_HOST)
                .replace("8080", BuildConfig.BASE_PORT)
            Picasso.get().load(imageNorm).into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}