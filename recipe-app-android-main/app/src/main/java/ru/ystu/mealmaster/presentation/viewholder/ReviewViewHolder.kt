package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Review
import java.text.SimpleDateFormat
import java.util.*

class ReviewViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private val userName = itemView.findViewById<TextView>(R.id.user_review)
    private val rating = itemView.findViewById<RatingBar>(R.id.ratingBar_review)
    private val dateText = itemView.findViewById<TextView>(R.id.date_review)
    private val reviewText = itemView.findViewById<TextView>(R.id.text_review)

    @SuppressLint("SetTextI18n")
    fun bind(review: Review) {
        userName.text = review.author

        try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val targetFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val date = originalFormat.parse(review.date)
            date?.let {
                val formattedDate = targetFormat.format(it)
                dateText.text = formattedDate
            }
        } catch (e: Exception) {
            Log.e("DateFormatError", "Error in date formatting", e)
        }

        reviewText.text = review.text
        rating.rating = review.rating.toFloat()
    }
}