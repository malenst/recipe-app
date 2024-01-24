package ru.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.dto.RecipeDTO
import ru.ystu.mealmaster.presentation.viewholder.RecipesByCategoryViewHolder

class RecipesByCategoryAdapter(private var recipeDTOS: List<RecipeDTO>) : RecyclerView.Adapter<RecipesByCategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesByCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_design_by_category, parent, false)
        return RecipesByCategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipesByCategoryViewHolder, position: Int) {
        val recipe = recipeDTOS[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipeDTOS.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipeDTOS: List<RecipeDTO>) {
        recipeDTOS = newRecipeDTOS
        notifyDataSetChanged()
    }
}