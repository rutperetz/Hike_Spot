package com.example.hike_spot

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // Initialize NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Set up bottom navigation
        bottomNav.setupWithNavController(navController)

        // Hide bottom navigation on auth screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.login, R.id.sign_up -> bottomNav.visibility = View.GONE
                else -> bottomNav.visibility = View.VISIBLE
            }
        }

        // Connect Bottom Navigation with NavController
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        // Handle navigation item selection with back stack management
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack(R.id.feed, false)
                    navController.navigate(R.id.feed)
                    true
                }
                R.id.navigation_search -> {
                    navController.popBackStack(R.id.search, false)
                    navController.navigate(R.id.search)
                    true
                }
                R.id.navigation_add -> {
                    navController.popBackStack(R.id.new_post, false)
                    navController.navigate(R.id.new_post)
                    true
                }
                R.id.navigation_profile -> {
                    navController.popBackStack(R.id.personal_feed, false)
                    navController.navigate(R.id.personal_feed)
                    true
                }
                else -> false
            }
        }
    }

    // Override back button behavior
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Handle system back button
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}