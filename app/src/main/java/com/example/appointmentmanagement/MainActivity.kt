package com.example.appointmentmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.appointmentmanagement.databinding.ActivityMainBinding
import com.example.appointmentmanagement.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve NavController from the NavHostFragment container
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Top-level destinations: on these screens, show hamburger icon; on all others, show back arrow
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_book_appointment,
                R.id.nav_my_appointments
            ),
            binding.drawerLayout
        )

        // Bind Toolbar directly to NavController with AppBarConfiguration
        // This automatically handles toggling between the hamburger icon and back arrow
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Bind Drawer NavigationView to NavController
        binding.navView.setupWithNavController(navController)

        // Ensure clicking an item in the drawer closes the drawer after navigating
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
            if (handled) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            handled
        }

        setupDrawerHeaderEmail()
        setupMenu()
    }

    private fun setupDrawerHeaderEmail() {
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "patient@example.com"
        val headerView = binding.navView.getHeaderView(0)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.tvHeaderEmail)
        tvHeaderEmail.text = userEmail
    }

    private fun setupMenu() {
        // Inflate toolbar options menu directly on the MaterialToolbar
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About CarePoint Clinic")
            .setMessage("CarePoint Appointment Management System\nVersion 1.0\nTheme 1: Health and Wellness for All\nStrathmore University")
            .setPositiveButton("OK", null)
            .show()
    }

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
}