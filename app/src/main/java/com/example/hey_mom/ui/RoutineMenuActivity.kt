package com.example.hey_mom.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.R
import android.content.Intent
import com.google.android.material.card.MaterialCardView

class RoutineMenuActivity : AppCompatActivity() {

    private lateinit var babyId: String
    private lateinit var babyName: String
    private lateinit var babyDob: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_menu)

        babyId = intent.getStringExtra("baby_id") ?: ""
        if (babyId.isEmpty()) {
            Toast.makeText(this, "Invalid baby", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Optional: Set baby name if passed
        babyName = intent.getStringExtra("baby_name") ?: "Baby"
        babyDob = intent.getStringExtra("baby_dob") ?: ""

        findViewById<TextView>(R.id.tvRoutineTitle).text = "$babyName's Routines"

        // Updated to use MaterialCardView instead of Button
        findViewById<MaterialCardView>(R.id.btnFeedingRoutine).setOnClickListener {
            startRoutineActivity(FeedingActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.btnSleepRoutine).setOnClickListener {
            startRoutineActivity(SleepActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.btnDiaperRoutine).setOnClickListener {
            startRoutineActivity(DiaperActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.btnGrowthTracking).setOnClickListener {
            startRoutineActivity(GrowthTrackingActivity::class.java)
        }

        findViewById<MaterialCardView>(R.id.btnVaccineSchedule).setOnClickListener {
            startRoutineActivity(VaccineScheduleActivity::class.java)
        }
    }

    private fun startRoutineActivity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        intent.putExtra("baby_id", babyId)
        intent.putExtra("baby_dob", babyDob)
        startActivity(intent)
    }
}