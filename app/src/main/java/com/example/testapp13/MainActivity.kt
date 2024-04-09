package com.example.testapp13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {

    private var currentTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    private lateinit var themeToggleImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() //эта штука нужна, чтоб скрыть название активити сверху

        themeToggleImageView = findViewById(R.id.theme_toggle_image_view) // Initialize
        updateThemeToggleImage() // Set initial image based on current theme
        themeToggleImageView.contentDescription = getString(R.string.theme_toggle_description)
        themeToggleImageView.setOnClickListener {
            toggleTheme()
        }

        val newProfileButton = findViewById<Button>(R.id.new_profile_button)
        val loadProfileButton = findViewById<Button>(R.id.load_profile_button)

        // Set click listener on the button
        newProfileButton.setOnClickListener {
            // Create an intent to launch SecondActivity
            val intent = Intent(this, SecondActivity::class.java)
            // Start the activity
            startActivity(intent)
        }

        loadProfileButton.setOnClickListener {
            // Create an intent to launch FifthActivity
            val intent = Intent(this, FifthActivity::class.java)
            // Start the activity
            startActivity(intent)
        }
    }
        // Create options menu
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }

        // Handle menu item selection
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.toggle_theme -> {
                    toggleTheme()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        private fun toggleTheme() {
            currentTheme = when (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_YES
                AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(currentTheme)
            updateThemeToggleImage() // Update image after theme change
        }

        private fun updateThemeToggleImage() {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                themeToggleImageView.setImageResource(R.drawable.moon) // Dark theme image
            } else {
                themeToggleImageView.setImageResource(R.drawable.sun) // Light theme image
            }
        }
}

