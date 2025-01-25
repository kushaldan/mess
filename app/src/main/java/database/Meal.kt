package com.kushal.mealapp.database

//noinspection SuspiciousImport
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val date: String,
    val name: String,
    val amount: Double
)

@Entity(tableName = "deposit")
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val name: String,
    val date: String,
    val amount: Double
)
