package com.example.hey_mom

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Vaccine_Activity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_vaccine)
        //for status bar
        val window = window
        window.statusBarColor = Color.WHITE // or ContextCompat.getColor(requireContext(), R.color.your_bg)
        setLightStatusBar(window, isLight = true)


            val webView: WebView = findViewById(R.id.webView)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.uptodate.com/contents/image?imageKey=PI/60399")
        }
    }

//for status bar

fun setLightStatusBar(window: Window, isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        if (controller != null) {
            if (isLight) {
                controller.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                controller.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            if (isLight) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
    }
}