package ru.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.domain.Recipe
import ru.ystu.mealmaster.presentation.viewholder.PopViewHolder

class PopRecipeAdapter(private var recipes: List<Recipe>) : RecyclerView.Adapter<PopViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.popular_list, parent, false)
        return PopViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PopViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}