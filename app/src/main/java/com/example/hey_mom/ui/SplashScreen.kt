package com.example.hey_mom.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.R
import com.example.hey_mom.utils.SessionManager

class SplashScreen : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        val videoView: VideoView = findViewById(R.id.splash_video)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash}")

        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener {
            // Check login status when video completes
            checkLoginStatus()
        }

        videoView.start()
    }

    private fun checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // User is already logged in, go to homepage
            startActivity(Intent(this, Homepage::class.java))
        } else {
            // User is not logged in, go to signin
            startActivity(Intent(this, Signin::class.java))
        }
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted - can post notifications
            } else {
                // Show rationale or direct to settings
                Toast.makeText(this, "Notification permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}