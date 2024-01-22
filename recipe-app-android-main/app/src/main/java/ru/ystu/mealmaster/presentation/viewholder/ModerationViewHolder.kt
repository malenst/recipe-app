package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.enumeration.ChangeType
import ru.ystu.mealmaster.presentation.activity.ModRecipeActivity
import ru.ystu.mealmaster.presentation.activity.RecipeActivity

class ModerationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.nameModeration)
    //private val avgRating = itemView.findViewById<TextView>(R.id.listRatingByCategory)
    private val cookingTimeTextView = itemView.findViewById<TextView>(R.id.cookingTimeModeration)
    private val imageView = itemView.findViewById<ImageView>(R.id.imgModeration)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(recipe: Recipe) {
        context = itemView.context



        nameTextView.text = recipe.name
        cookingTimeTextView.text = recipe.cookingTime

        val recipeChangeType = recipe.changeType ?: ChangeType.CREATE

        itemView.setOnClickListener {
            intent = Intent(context, ModRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id.toString())
            Log.d("HREN", recipeChangeType.toString())
            intent.putExtra("CHANGE_TYPE", recipeChangeType.toString())
            context.startActivity(intent)
        }

        val moderationChangeType = itemView.findViewById<TextView>(R.id.listModerationChangeType)
        val layout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutModerationStatus)

        when (recipeChangeType) {
            ChangeType.CREATE -> {
                layout.background = ContextCompat.getDrawable(context, R.drawable.status_add)
                moderationChangeType.text = ChangeType.CREATE.value
            }
            ChangeType.UPDATE -> {
                layout.background = ContextCompat.getDrawable(context, R.drawable.status_update)
                moderationChangeType.text = ChangeType.UPDATE.value
            }
            ChangeType.DELETE -> {
                layout.background = ContextCompat.getDrawable(context, R.drawable.status_delete)
                moderationChangeType.text = ChangeType.DELETE.value
            }
        }

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