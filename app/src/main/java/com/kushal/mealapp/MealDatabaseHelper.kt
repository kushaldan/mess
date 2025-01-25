package com.kushal.mealapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MealDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MealDatabase.db"
        private const val DATABASE_VERSION = 1

        // Table and column names
        const val TABLE_NAME = "Meals"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_MEAL = "meal"
        const val COLUMN_DATE = "date"
        const val COLUMN_AMOUNT = "amount" // Added amount column
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createTableQuery = """
                CREATE TABLE $TABLE_NAME (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NAME TEXT NOT NULL,
                    $COLUMN_MEAL TEXT NOT NULL,
                    $COLUMN_DATE TEXT NOT NULL,
                    $COLUMN_AMOUNT REAL NOT NULL
                );
            """
            db.execSQL(createTableQuery)
        } catch (e: Exception) {
            Log.e("MealDatabaseHelper", "Error creating database: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        } catch (e: Exception) {
            Log.e("MealDatabaseHelper", "Error upgrading database: ${e.message}")
        }
    }

    fun insertMeal(name: String, meal: String, date: String): Boolean {
        val db = writableDatabase
        return try {
            val contentValues = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_MEAL, meal)
                put(COLUMN_DATE, date)
                 // Insert the amount value
            }
            val result = db.insert(TABLE_NAME, null, contentValues)
            result != -1L  // Return true if insert is successful
        } catch (e: Exception) {
            Log.e("MealDatabaseHelper", "Error inserting meal: ${e.message}")
            false  // Return false if an exception occurs
        } finally {
            db.close()
        }
    }

    fun getAllMealsAsMap(): List<Map<String, String>> {
        val meals = mutableListOf<Map<String, String>>()
        val db = readableDatabase

        try {
            val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            cursor.use {
                if (cursor.moveToFirst()) {
                    do {
                        val mealData = mapOf(
                            COLUMN_NAME to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                            COLUMN_MEAL to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL)),
                            COLUMN_DATE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                            COLUMN_AMOUNT to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)) // Add amount to map
                        )
                        meals.add(mealData)
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e("MealDatabaseHelper", "Error fetching meals as map: ${e.message}")
        } finally {
            db.close()
        }
        return meals
    }

    fun getAllMeals(): List<Meal> {
        val mealList = mutableListOf<Meal>()
        val db = readableDatabase

        try {
            val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
            cursor.use {
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                        val meal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL))
                        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                        val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)) // Fetch amount as Double
                        mealList.add(Meal(id, name, meal, date, amount)) // Create Meal object with amount
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e("MealDatabaseHelper", "Error fetching meals: ${e.message}")
        } finally {
            db.close()
        }
        return mealList
    }
}
