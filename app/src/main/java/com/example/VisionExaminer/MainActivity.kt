package com.example.VisionExaminer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var themeToggleFrameLayout: FrameLayout
    private lateinit var videoView: VideoView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set status bar color and transparency BEFORE setContentView()
        WindowCompat.setDecorFitsSystemWindows(window, false) // Hide status bar

        setContentViewBasedOnOrientation()

        supportActionBar?.hide()

        themeToggleFrameLayout = findViewById(R.id.theme_toggle_frame_layout)
        videoView = findViewById(R.id.video_view)

        viewModel.isNightMode.observe(this) { isNightMode ->
            updateUIForTheme(isNightMode)
        }

        viewModel.videoUri.observe(this) { uri ->
            setVideoUri(uri)
        }

        themeToggleFrameLayout.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    updateThemeButton(viewModel.isNightMode.value ?: true, true) // Show pressed state
                    true // Consume touch event
                }
                android.view.MotionEvent.ACTION_UP -> {
                    viewModel.toggleTheme() // Toggle theme
                    themeToggleFrameLayout.performClick() // Call performClick()
                    true // Consume touch event
                }
                else -> false // Don't consume other events
            }
        }

        findViewById<FrameLayout>(R.id.new_profile_button).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("isNightMode", viewModel.isNightMode.value) // Передача isNightMode
            startActivity(intent)
        }

        findViewById<FrameLayout>(R.id.load_profile_button).setOnClickListener {
            val intent = Intent(this, FifthActivity::class.java)
            intent.putExtra("isNightMode", viewModel.isNightMode.value) // Передача isNightMode
            startActivity(intent)
        }

        themeToggleFrameLayout.isSelected = viewModel.isNightMode.value ?: true
    }

    private fun setContentViewBasedOnOrientation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape)
        } else {
            setContentView(R.layout.activity_main)
        }
    }

    private fun updateUIForTheme(isNightMode: Boolean) {
        setTheme(if (isNightMode) R.style.AppTheme_Night else R.style.AppTheme_Day)
        window.statusBarColor = ContextCompat.getColor(this, if (isNightMode) R.color.black else R.color.dayStatusBar)
        themeToggleFrameLayout.background = ContextCompat.getDrawable(this, if (isNightMode) R.drawable.text_frame_night_background else R.drawable.text_frame_day_background)
        updateThemeButton(isNightMode, false)
    }

    private fun updateThemeButton(isNightMode: Boolean, isPressed: Boolean) {
        val resourceId = if (isNightMode) {
            if (isPressed) R.drawable.night_blue_pressed else R.drawable.night_blue
        } else {
            if (isPressed) R.drawable.day_red_pressed else R.drawable.day_red
        }
        themeToggleFrameLayout.background = ContextCompat.getDrawable(this, resourceId)
    }

    private fun setVideoUri(uri: Uri) {
        videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.start()
        }
    }
}
