package com.kushal.mealapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealRecyclerView : AppCompatActivity() {

    private lateinit var mealRecyclerView: RecyclerView
    private lateinit var mealDatabaseHelper: MealDatabaseHelper
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)

        // Initialize views
        mealRecyclerView = findViewById(R.id.mealRecyclerView)

        // Initialize database helper
        mealDatabaseHelper = MealDatabaseHelper(this)

        // Fetch data from the database
        val mealList = mealDatabaseHelper.getAllMeals()

        if (mealList.isNotEmpty()) {
            // Set up RecyclerView
            mealAdapter = MealAdapter(mealList)
            mealRecyclerView.layoutManager = LinearLayoutManager(this)
            mealRecyclerView.adapter = mealAdapter
        } else {
            // Show a message if no data is found
            Toast.makeText(this, "No meals found in the database", Toast.LENGTH_SHORT).show()
        }
    }
}
