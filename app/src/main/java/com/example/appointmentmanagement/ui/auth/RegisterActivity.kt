package com.example.appointmentmanagement.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentmanagement.databinding.ActivityRegisterBinding
import android.content.Intent
/**
 * Handles new patient registration with input validation.
 */
class RegisterActivity : AppCompatActivity() {

    // View Binding reference generated from activity_register.xml
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate view binding layout
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    /**
     * Attaches click handlers to UI buttons and navigation links.
     */
    private fun setupListeners() {
        // Register button click
        binding.btnRegister.setOnClickListener {
            handleRegistration()
        }

        // Return to login screen
        binding.tvGoToLogin.setOnClickListener {
            finish()
        }
    }

    /**
     * Validates user inputs before processing registration.
     */
    private fun handleRegistration() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val phone = binding.etRegisterPhone.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        var isValid = true

        // Validate Full Name
        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Full name is required"
            isValid = false
        } else {
            binding.tilFullName.error = null
        }

        // Validate Email
        if (email.isEmpty()) {
            binding.tilRegisterEmail.error = "Email is required"
            isValid = false
        } else if (!email.contains("@") || !email.contains(".")) {
            binding.tilRegisterEmail.error = "Please enter a valid email address"
            isValid = false
        } else {
            binding.tilRegisterEmail.error = null
        }

        // Validate Phone Number
        if (phone.isEmpty()) {
            binding.tilRegisterPhone.error = "Phone number is required"
            isValid = false
        } else if (phone.length < 10) {
            binding.tilRegisterPhone.error = "Phone number must be at least 10 digits"
            isValid = false
        } else {
            binding.tilRegisterPhone.error = null
        }

        // Validate Password
        if (password.isEmpty()) {
            binding.tilRegisterPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.tilRegisterPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.tilRegisterPassword.error = null
        }

        // Validate Confirm Password match
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        // Stop if any validation checks failed
        if (!isValid) return

        // Registration successful message
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

        // Create an Intent to return the registered email back to LoginActivity
        val resultIntent = Intent().apply {
            putExtra("REGISTERED_EMAIL", email)
        }
        // Set the activity result code and bundle data
        setResult(RESULT_OK, resultIntent)

        // Close RegisterActivity and return to LoginActivity
        finish()
    }
}