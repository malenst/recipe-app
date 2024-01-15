package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.presentation.activity.AddReviewActivity
import ru.ystu.mealmaster.presentation.activity.RecipeActivity

class RecipesByCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.nameByCategory)
    private val avgRating = itemView.findViewById<TextView>(R.id.listRatingByCategory)
    private val cookingTimeTextView = itemView.findViewById<TextView>(R.id.cookingTimeByCategory)
    private val imageView = itemView.findViewById<ImageView>(R.id.imgByCategory)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(recipe: Recipe) {
        itemView.setOnClickListener {
            context = itemView.context
            intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id.toString())
            context.startActivity(intent)
        }

        nameTextView.text = recipe.name
        if (recipe.reviews.isNullOrEmpty()) {
            avgRating.text = "Оставить"
            avgRating.textSize = 12F
            avgRating.setOnClickListener{
                context = itemView.context
                intent = Intent(context, AddReviewActivity::class.java)
                intent.putExtra("RECIPE_ID", recipe.id.toString())
                context.startActivity(intent)
            }

            val button = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutByCategoryRating)
            button.setOnClickListener {
                Log.d("HBB", "HERE")
            }
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