package com.kushal.mealapp

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: Deposit)

    @Query("SELECT * FROM meal ORDER BY date DESC")
    fun getAllMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM deposit ORDER BY date DESC")
    fun getAllDeposits(): Flow<List<Deposit>>
}
