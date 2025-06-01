package com.example.hey_mom.ui

import android.app.DatePickerDialog
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
import com.example.hey_mom.notifications.NotificationScheduler
import com.example.hey_mom.repository.VaccineRepository
import com.example.hey_mom.ui.adapters.VaccineAdapter
import com.example.hey_mom.viewmodel.VaccineViewModel
import com.example.hey_mom.viewmodel.VaccineViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class VaccineScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: VaccineViewModel
    private lateinit var babyId: String
    private var babyDob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_schedule)

        babyId = intent.getStringExtra("baby_id") ?: ""
        babyDob = intent.getStringExtra("baby_dob") ?: ""

        val api = ApiClient.retrofit.create(ApiService::class.java)
        val factory = VaccineViewModelFactory(VaccineRepository(api))
        viewModel = ViewModelProvider(this, factory)[VaccineViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerVaccines)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.babyVaccineList.observe(this) { vaccineList ->
            recycler.adapter = VaccineAdapter(vaccineList) { selected ->
                showMarkDoneDialog(selected)
            }
            scheduleVaccineReminders(vaccineList)
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == "success") viewModel.getBabyVaccines(babyId.toInt())
        }

        viewModel.getBabyVaccines(babyId.toInt())
    }

    private fun showMarkDoneDialog(vaccine: BabyVaccineStatus) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mark_done, null)
        val editDate = dialogView.findViewById<EditText>(R.id.editDate)
        val editNotes = dialogView.findViewById<EditText>(R.id.editNotes)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = sdf.format(Date())
        editDate.setText(today)

        editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    editDate.setText(sdf.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Mark Vaccine as Completed")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val date = editDate.text.toString()
                val notes = editNotes.text.toString()

                if (date.isBlank()) {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.updateVaccineStatus(
                    babyId.toInt(),
                    vaccine.vaccine_id,
                    "Completed",
                    date,
                    notes
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun scheduleVaccineReminders(vaccineList: List<BabyVaccineStatus>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dobDate = try {
            sdf.parse(babyDob)
        } catch (e: Exception) {
            null
        } ?: return

        for (vaccine in vaccineList) {
            if (vaccine.status == "Pending") {
                val reminderDate = Calendar.getInstance().apply {
                    time = dobDate
                    add(Calendar.WEEK_OF_YEAR, vaccine.recommended_weeks)
                }

                if (reminderDate.after(Calendar.getInstance())) {
                    NotificationScheduler.scheduleOneTimeAlarm(
                        context = this,
                        id = vaccine.vaccine_id + babyId.toInt(),
                        triggerAtMillis = reminderDate.timeInMillis,
                        type = "Vaccine: ${vaccine.vaccine_name}"
                    )
                }
            }
        }
    }
}
