package ru.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.dto.ReviewDTO
import ru.ystu.mealmaster.presentation.viewholder.ReviewViewHolder

class ReviewAdapter (private var reviewDTOS: List<ReviewDTO>) : RecyclerView.Adapter<ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_review, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewDTOS[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviewDTOS.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipes: List<ReviewDTO>) {
        reviewDTOS = newRecipes
        notifyDataSetChanged()
    }
}