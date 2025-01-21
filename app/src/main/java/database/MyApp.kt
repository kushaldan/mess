package com.kushal.mealapp.database
import android.app.Application
import androidx.room.Room

class MyApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
