package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var remarkEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var userNameText: EditText  // Added userNameText reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        // Find views by ID
        nameEditText = findViewById(R.id.nameEditText)
        remarkEditText = findViewById(R.id.remarkEditText)
        submitButton = findViewById(R.id.submitButton)


    }}