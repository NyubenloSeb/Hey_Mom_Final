package com.example.hey_mom
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

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
}
