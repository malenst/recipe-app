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
import ru.ystu.mealmaster.presentation.activity.OwnRecipeActivity

class MyRecipesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView = itemView.findViewById<TextView>(R.id.search_txt)
    //private val avgRating = itemView.findViewById<TextView>(R.id.listRatingByCategory)
    private val rating = itemView.findViewById<TextView>(R.id.list_rating)
    private val imageView = itemView.findViewById<ImageView>(R.id.search_img)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(recipeDTO: RecipeDTO) {
        context = itemView.context

        itemView.setOnClickListener {
            intent = Intent(context, OwnRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipeDTO.id.toString())
            intent.putExtra("CHANGE_TYPE", recipeDTO.changeType.toString())
            context.startActivity(intent)
        }

        nameTextView.text = recipeDTO.name


        if (!recipeDTO.reviewDTOS.isNullOrEmpty()) {
            rating.visibility = View.VISIBLE
            val averageRating = recipeDTO.reviewDTOS.map { it.rating }.average()
            Log.d("TIKVA", averageRating.toString())
            rating.text = String.format("%.1f", averageRating.toFloat())

        }

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