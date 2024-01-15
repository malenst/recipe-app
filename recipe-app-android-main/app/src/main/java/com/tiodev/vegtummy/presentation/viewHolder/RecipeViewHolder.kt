package com.tiodev.vegtummy.presentation.viewHolder

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tiodev.vegtummy.R
import com.tiodev.vegtummy.presentation.activity.RecipeActivity
import com.tiodev.vegtummy.domain.Recipe

class RecipeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val nameTextView = itemView.findViewById<TextView>(R.id.txt1)

    private val avgRating = itemView.findViewById<TextView>(R.id.list_rating)

    private val cookingTimeTextView = itemView.findViewById<TextView>(R.id.cooking_time)
    private val imageView = itemView.findViewById<ImageView>(R.id.img)

    @SuppressLint("SetTextI18n")
    fun bind(recipe: Recipe) {
        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id.toString())
            context.startActivity(intent)
        }

        nameTextView.text = recipe.name
        val ingredientsFormatted = recipe.ingredients.entries.joinToString(separator = "\n") { (key, value) ->
            "• $key, $value"
        }
       val stepsFormatted = recipe.steps.entries.joinToString(separator = "\n") { (key, value) ->
            "$key) $value"
        }
        if (recipe.reviews.isNullOrEmpty()) {
            avgRating.visibility = View.GONE
        } else {
            avgRating.visibility = View.VISIBLE
           val averageRating = recipe.reviews.map { it.rating }.average()
            Log.d("TIKVA", averageRating.toString())
            avgRating.text = String.format("%.1f", averageRating.toFloat())

        }
        cookingTimeTextView.text = recipe.cookingTime

        if (!recipe.image.isNullOrEmpty()) {
            val imageNorm = recipe.image.replace("localhost", "10.0.2.2")
            Picasso.get().load(imageNorm).into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}