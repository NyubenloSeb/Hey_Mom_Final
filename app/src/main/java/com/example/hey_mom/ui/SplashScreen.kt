package com.example.hey_mom.ui
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val videoView: VideoView = findViewById(R.id.splash_video)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash}")

        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener {
            startActivity(Intent(this, Signin::class.java))
            finish()
        }

        videoView.start()
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
