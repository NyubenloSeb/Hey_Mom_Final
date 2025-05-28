package com.example.hey_mom.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
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
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

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


        //for date

        val etDob = findViewById<TextInputEditText>(R.id.etDob)

        etDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etDob.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }



        // Bind UI components
        val bloodGroup=findViewById<RadioGroup>(R.id.rgBloodGroup)
        val name = findViewById<EditText>(R.id.etName)
        val etWeight = findViewById<TextInputEditText>(R.id.etWeight)
        val genderGroup = findViewById<RadioGroup>(R.id.rgGender)
        val height = findViewById<EditText>(R.id.etHeight)
        val allergies = findViewById<EditText>(R.id.etAllergies)
        val conditions = findViewById<EditText>(R.id.etConditions)

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {

            val selectedBloodGroupId = bloodGroup.checkedRadioButtonId
            val blood = if (selectedBloodGroupId != -1) {
                val selectedBloodRadioButton = findViewById<RadioButton>(selectedBloodGroupId)
                selectedBloodRadioButton.text.toString()
            } else {
                ""
            }


            val selectedGenderId = genderGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedGenderId)
                selectedRadioButton.text.toString()
            } else {
                ""
            }

            if (name.text.isEmpty() || gender.isEmpty() || etDob.text.isNullOrEmpty()) {
                Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val baby = Baby(
                baby_id = 0,
                name = name.text.toString(),
                dob = etDob.text.toString(),
                gender = gender,
                weight_kg = etWeight.text.toString().toFloatOrNull() ?: 0f,
                height_cm = height.text.toString().toFloatOrNull() ?: 0f,
                blood_group = blood,
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
