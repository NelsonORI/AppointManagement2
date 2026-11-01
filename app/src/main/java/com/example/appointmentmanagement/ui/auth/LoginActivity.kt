package com.example.appointmentmanagement.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentmanagement.MainActivity
import com.example.appointmentmanagement.databinding.ActivityLoginBinding

/**
 * Entry screen of the application handling patient authentication.
 */
class LoginActivity : AppCompatActivity() {

    // View Binding reference generated from activity_login.xml
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate view binding layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    /**
     * Attaches click handlers to UI buttons and navigation links.
     */
    private fun setupListeners() {
        // Sign In button action
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // Navigate to registration screen
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Validates credentials and routes to the main app dashboard.
     */
    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Input validation matching lab patterns
        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return
        } else {
            binding.tilPassword.error = null
        }

        // In-memory demo authentication check
        if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            return
        }

        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

        // Explicit Intent navigating to MainActivity, passing authenticated email
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_EMAIL", email)
            // Clear task so user cannot navigate back to login screen with back button
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}