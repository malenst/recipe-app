package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.presentation.activity.AddReviewActivity
import ru.ystu.mealmaster.presentation.activity.RecipeActivity

class RecipeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val nameTextView = itemView.findViewById<TextView>(R.id.txt1)

    private val avgRating = itemView.findViewById<TextView>(R.id.list_rating)

    private val cookingTimeTextView = itemView.findViewById<TextView>(R.id.cooking_time)
    private val imageView = itemView.findViewById<ImageView>(R.id.img)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(recipeDTO: RecipeDTO) {
        itemView.setOnClickListener {
            context = itemView.context
            intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeDTO.id.toString())
            context.startActivity(intent)
        }

        nameTextView.text = recipeDTO.name

        if (recipeDTO.reviewDTOS.isNullOrEmpty()) {
            avgRating.text = "Оставить"
            avgRating.textSize = 12F
            avgRating.setOnClickListener{
                context = itemView.context
                intent = Intent(context, AddReviewActivity::class.java)
                intent.putExtra("RECIPE_ID", recipeDTO.id.toString())
                context.startActivity(intent)
            }
        } else {
            avgRating.visibility = View.VISIBLE
           val averageRating = recipeDTO.reviewDTOS.map { it.rating }.average()
            Log.d("TIKVA", averageRating.toString())
            avgRating.text = String.format("%.1f", averageRating.toFloat())

        }
        cookingTimeTextView.text = recipeDTO.cookingTime

        if (!recipeDTO.image.isNullOrEmpty()) {
            val imageNorm = recipeDTO.image
                .replace("http", BuildConfig.BASE_PROTOCOL)
                .replace("localhost", BuildConfig.BASE_HOST)
                .replace("8080", BuildConfig.BASE_PORT)
            Picasso.get().load(imageNorm).into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}