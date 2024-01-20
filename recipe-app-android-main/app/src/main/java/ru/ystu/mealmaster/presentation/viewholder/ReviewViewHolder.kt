package ru.ystu.mealmaster.presentation.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Review
import ru.ystu.mealmaster.presentation.activity.AddReviewActivity

class ReviewViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private val userName = itemView.findViewById<TextView>(R.id.user_review)
    private val rating = itemView.findViewById<RatingBar>(R.id.ratingBar_review)
    private val date = itemView.findViewById<TextView>(R.id.date_review)
    private val reviewText = itemView.findViewById<TextView>(R.id.text_review)

    private lateinit var context: Context
    private lateinit var intent: Intent

    @SuppressLint("SetTextI18n")
    fun bind(review: Review) {

        userName.text = review.author
        date.text = review.date
        reviewText.text = review.text
        rating.rating = review.rating.toFloat()!!



//        if (review.reviews.isNullOrEmpty()) {
//            rating.text = "Оставить"
//            rating.textSize = 12F
//            rating.setOnClickListener{
//                context = itemView.context
//                intent = Intent(context, AddReviewActivity::class.java)
//                intent.putExtra("RECIPE_ID", review.id.toString())
//                context.startActivity(intent)
//            }
//
//            val button = itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutByCategoryRating)
//            button.setOnClickListener {
//                Log.d("HBB", "HERE")
//            }
//        } else {
//            rating.visibility = View.VISIBLE
//            val averageRating = recipe.reviews.map { it.rating }.average()
//            Log.d("TIKVA", averageRating.toString())
//            rating.text = String.format("%.1f", averageRating.toFloat())
//
//        }
//        date.text = recipe.cookingTime
    }
}