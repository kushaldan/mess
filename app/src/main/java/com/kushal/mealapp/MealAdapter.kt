package com.kushal.mealapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealAdapter(private val mealList: List<Meal>) :
    RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    // ViewHolder class to hold and manage item views

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val mealTextView: TextView = itemView.findViewById(R.id.mealTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        // Inflate the layout for each item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        // Bind data to the views
        val meal = mealList[position]
        holder.nameTextView.text = meal.name
        holder.mealTextView.text = meal.meal
        holder.dateTextView.text = meal.date
    }

    override fun getItemCount(): Int = mealList.size // Return the size of the list
}
