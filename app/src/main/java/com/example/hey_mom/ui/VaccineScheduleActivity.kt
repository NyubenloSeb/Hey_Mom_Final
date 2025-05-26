package com.example.hey_mom.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.BabyVaccineStatus
import com.example.hey_mom.repository.VaccineRepository
import com.example.hey_mom.ui.adapters.VaccineAdapter
import com.example.hey_mom.viewmodel.VaccineViewModel
import com.example.hey_mom.viewmodel.VaccineViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VaccineScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: VaccineViewModel
    private lateinit var babyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_schedule)

        babyId = intent.getStringExtra("baby_id") ?: ""
        val api = ApiClient.retrofit.create(ApiService::class.java)
        val factory = VaccineViewModelFactory(VaccineRepository(api))
        viewModel = ViewModelProvider(this, factory)[VaccineViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerVaccines)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.babyVaccineList.observe(this) {
            recycler.adapter = VaccineAdapter(it) { selected ->
                showMarkDoneDialog(selected)
            }
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == "success") viewModel.getBabyVaccines(babyId.toInt())
        }

        viewModel.getBabyVaccines(babyId.toInt())
    }

    private fun showMarkDoneDialog(vaccine: BabyVaccineStatus) {
        val input = EditText(this)
        input.hint = "Enter notes (optional)"

        AlertDialog.Builder(this)
            .setTitle("Mark Vaccine as Completed")
            .setMessage("Enter administration date (yyyy-MM-dd)")
            .setView(input)
            .setPositiveButton("Submit") { _, _ ->
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                viewModel.updateVaccineStatus(
                    babyId.toInt(),
                    vaccine.vaccine_id,
                    "Completed",
                    date,
                    input.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
