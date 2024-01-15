package com.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ystu.mealmaster.R
import com.ystu.mealmaster.domain.Category
import com.ystu.mealmaster.presentation.viewholder.CatViewHolder

class CatRecipeAdapter(private var categories: List<Category>) : RecyclerView.Adapter<CatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.category_list, parent, false)
        return CatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}