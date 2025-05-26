package com.example.hey_mom.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.Baby
import com.example.hey_mom.repository.BabyRepository
import com.example.hey_mom.viewmodel.BabyViewModel
import com.example.hey_mom.viewmodel.BabyViewModelFactory

class AddBabyActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyViewModel
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_baby)

        // Initialize ViewModel with factory
        val api: ApiService = ApiClient.retrofit.create(ApiService::class.java)
        val repository = BabyRepository(api)
        val factory = BabyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BabyViewModel::class.java]

        // Retrieve user ID from shared preferences
        userId = getSharedPreferences("HeyMomPrefs", MODE_PRIVATE)
            .getString("user_id", "") ?: ""

        // Bind UI components
        val name = findViewById<EditText>(R.id.etName)
        val dob = findViewById<EditText>(R.id.etDob)
        val gender = findViewById<EditText>(R.id.etGender)
        val weight = findViewById<EditText>(R.id.etWeight)
        val height = findViewById<EditText>(R.id.etHeight)
        val blood = findViewById<EditText>(R.id.etBloodGroup)
        val allergies = findViewById<EditText>(R.id.etAllergies)
        val conditions = findViewById<EditText>(R.id.etConditions)

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            if (name.text.isEmpty() || dob.text.isEmpty() || gender.text.isEmpty()) {
                Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val baby = Baby(
                baby_id = 0,
                name = name.text.toString(),
                dob = dob.text.toString(),
                gender = gender.text.toString(),
                weight_kg = weight.text.toString().toFloatOrNull() ?: 0f,
                height_cm = height.text.toString().toFloatOrNull() ?: 0f,
                blood_group = blood.text.toString(),
                known_allergies = allergies.text.toString(),
                medical_conditions = conditions.text.toString()
            )

            viewModel.addBaby(baby, userId.toInt(), "Mother")
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it.equals("success", ignoreCase = true)) {
                finish()
            }
        }
    }
}
