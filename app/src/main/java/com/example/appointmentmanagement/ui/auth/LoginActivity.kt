package com.example.appointmentmanagement.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentmanagement.MainActivity
import com.example.appointmentmanagement.databinding.ActivityLoginBinding

/**
 * Entry screen of the application handling patient authentication.
 */
class LoginActivity : AppCompatActivity() {

    // View Binding reference generated from activity_login.xml
    private lateinit var binding: ActivityLoginBinding

    // Contract launcher to receive the registered email back from RegisterActivity
    private val registerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Extract returned email extra
                val registeredEmail = result.data?.getStringExtra("REGISTERED_EMAIL")
                if (!registeredEmail.isNullOrEmpty()) {
                    // Pre-fill email field and place focus on password field
                    binding.etEmail.setText(registeredEmail)
                    binding.etPassword.requestFocus()
                    Toast.makeText(this, "Account created! Please sign in", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

        // Navigate to registration screen expecting a result
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            registerLauncher.launch(intent)
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