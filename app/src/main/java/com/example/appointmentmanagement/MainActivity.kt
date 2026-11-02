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

/**
 * Main shell activity hosting navigation fragments, the application toolbar,
 * and the navigation drawer.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set MaterialToolbar as Support Action Bar
        setSupportActionBar(binding.toolbar)

        // Retrieve NavController from the NavHostFragment container
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Top-level destinations where hamburger icon is displayed instead of Up arrow
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_book_appointment,
                R.id.nav_my_appointments
            ),
            binding.drawerLayout
        )

        // Connect Toolbar and Drawer with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        // Explicitly handle toolbar navigation icon click to open the drawer
        binding.toolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                val isTopLevel = appBarConfiguration.topLevelDestinations.contains(navController.currentDestination?.id)
                if (isTopLevel) {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    navController.navigateUp()
                }
            }
        }

        setupDrawerHeaderEmail()
        setupMenu()
    }

    /**
     * Reads user email passed from LoginActivity and displays it in the drawer header.
     */
    private fun setupDrawerHeaderEmail() {
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "patient@example.com"
        val headerView = binding.navView.getHeaderView(0)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.tvHeaderEmail)
        tvHeaderEmail.text = userEmail
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
     * Handles Up navigation fallback.
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}