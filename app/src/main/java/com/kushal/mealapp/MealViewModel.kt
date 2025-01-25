package com.kushal.mealapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MealViewModel(private val dao: MealDao) : ViewModel() {

    val allMeals: Flow<List<Meal>> = dao.getAllMeals()
    val allDeposits: Flow<List<Deposit>> = dao.getAllDeposits()

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            dao.insertMeal(meal)
        }
    }

    fun addDeposit(deposit: Deposit) {
        viewModelScope.launch {
            dao.insertDeposit(deposit)
        }
    }
}
