package ru.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.domain.Review
import ru.ystu.mealmaster.presentation.viewholder.ReviewViewHolder

class ReviewAdapter (private var reviews: List<Review>) : RecyclerView.Adapter<ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_review, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipes: List<Review>) {
        reviews = newRecipes
        notifyDataSetChanged()
    }
}