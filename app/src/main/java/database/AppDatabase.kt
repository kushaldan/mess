package com.kushal.mealapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Meal::class, Deposit::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}
