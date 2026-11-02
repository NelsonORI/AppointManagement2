package com.example.appointmentmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentmanagement.databinding.ActivityMainBinding

/**
 * Main shell activity hosting navigation fragments and the application toolbar.
 */
class MainActivity : AppCompatActivity() {

    // View Binding instance generated from activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate view binding layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set MaterialToolbar as the Support Action Bar
        setSupportActionBar(binding.toolbar)
    }
}