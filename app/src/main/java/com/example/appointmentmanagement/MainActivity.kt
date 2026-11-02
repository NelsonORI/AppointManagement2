package com.example.appointmentmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.appointmentmanagement.databinding.ActivityMainBinding
import com.example.appointmentmanagement.ui.auth.LoginActivity

/**
 * Main shell activity hosting navigation fragments and the application toolbar.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Retrieve NavController from the NavHostFragment container
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Automatically sync toolbar title and up/back button with current destination
        NavigationUI.setupActionBarWithNavController(this, navController)

        setupMenu()
    }

    /**
     * Attaches MenuProvider to supply toolbar options menu items.
     */
    private fun setupMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_about -> {
                        showAboutDialog()
                        true
                    }
                    R.id.action_logout -> {
                        confirmLogout()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    /**
     * Displays clinic information in an alert dialog.
     */
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About CarePoint Clinic")
            .setMessage("CarePoint Appointment Management System\nVersion 1.0\nTheme 1: Health and Wellness for All\nStrathmore University")
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Confirms and executes user session sign out.
     */
    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Handles the toolbar Up navigation button using NavController.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}