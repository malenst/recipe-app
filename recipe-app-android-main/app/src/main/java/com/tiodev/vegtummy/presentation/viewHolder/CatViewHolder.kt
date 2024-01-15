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
import com.tiodev.vegtummy.domain.Category
import com.tiodev.vegtummy.domain.Recipe
import com.tiodev.vegtummy.presentation.activity.RecipeActivity

class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.category_txt)
    private val imageView = itemView.findViewById<ImageView>(R.id.cat_img)

    @SuppressLint("SetTextI18n")
    fun bind(category: Category) {
        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, RecipeActivity::class.java)
            Log.d("REDKA", category.name)
            intent.putExtra("CATEGORY", category.name)
            context.startActivity(intent)
        }

        nameTextView.text = category.translation


        if (!category.image.isNullOrEmpty()) {
            val imageNorm = category.image.replace("localhost", "10.0.2.2")
            Picasso.get().load(imageNorm).into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}