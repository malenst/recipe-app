package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Category
import ru.ystu.mealmaster.presentation.activity.RecipesByCategoryActivity

class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.category_txt)
    private val imageView = itemView.findViewById<ImageView>(R.id.cat_img)

    @SuppressLint("SetTextI18n")
    fun bind(category: Category) {
        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, RecipesByCategoryActivity::class.java)

            intent.putExtra("CATEGORY", category.name)
            intent.putExtra("TRANSLATION", category.translation)
            context.startActivity(intent)
        }

        nameTextView.text = category.translation

        if (!category.image.isNullOrEmpty()) {
            val imageNorm = category.image
                .replace("http", BuildConfig.BASE_PROTOCOL)
                .replace("localhost", BuildConfig.BASE_HOST)
                .replace("8080", BuildConfig.BASE_PORT)
            Picasso.get().load(imageNorm).into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}