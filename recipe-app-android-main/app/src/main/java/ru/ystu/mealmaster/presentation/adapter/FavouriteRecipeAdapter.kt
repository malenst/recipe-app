package ru.ystu.mealmaster.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.entity.FavouriteRecipe
import ru.ystu.mealmaster.presentation.viewholder.FavouriteRecipeViewHolder

class FavouriteRecipeAdapter(private var recipes: List<FavouriteRecipe>) : RecyclerView.Adapter<FavouriteRecipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_design_favourite_recipe, parent, false)
        return FavouriteRecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavouriteRecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipes: List<FavouriteRecipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}