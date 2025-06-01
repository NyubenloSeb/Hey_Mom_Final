package com.example.hey_mom.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
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
import com.example.hey_mom.api.models.SleepEntry
import com.example.hey_mom.notifications.NotificationScheduler
import com.example.hey_mom.repository.SleepRepository
import com.example.hey_mom.ui.adapters.SleepAdapter
import com.example.hey_mom.viewmodel.SleepViewModel
import com.example.hey_mom.viewmodel.SleepViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class SleepActivity : AppCompatActivity() {

    private lateinit var viewModel: SleepViewModel
    private lateinit var babyId: String
    private var selectedCalendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        babyId = intent.getStringExtra("baby_id") ?: ""

        val api = ApiClient.retrofit.create(ApiService::class.java)
        val factory = SleepViewModelFactory(SleepRepository(api))
        viewModel = ViewModelProvider(this, factory)[SleepViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerSleep)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.sleepList.observe(this) {
            recycler.adapter = SleepAdapter(it, onEdit = { entry -> showEditSleepDialog(entry) })
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == "success") {
                viewModel.getSleepEntries(babyId.toInt())
                selectedCalendar?.let { calendar ->
                    NotificationScheduler.scheduleOneTimeAlarm(
                        context = this,
                        id = Random().nextInt(10000),
                        triggerAtMillis = calendar.timeInMillis,
                        type = "Sleep"
                    )
                    Toast.makeText(this, "Sleep reminder scheduled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getSleepEntries(babyId.toInt())

        val etStart = findViewById<EditText>(R.id.etSleepStart)
        val etEnd = findViewById<EditText>(R.id.etSleepEnd)
        val etNotes = findViewById<EditText>(R.id.etSleepNotes)

        etStart.setOnClickListener { showTimePicker(etStart) }
        etEnd.setOnClickListener { showTimePicker(etEnd) }

        findViewById<Button>(R.id.btnAddSleep).setOnClickListener {
            val startText = etStart.text.toString()
            val endText = etEnd.text.toString()
            val notes = etNotes.text.toString()

            if (startText.isEmpty() || endText.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse start time for notification
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val parsedDate = sdf.parse(startText)

            if (parsedDate != null) {
                val timeOnly = Calendar.getInstance().apply { time = parsedDate }
                selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timeOnly.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, timeOnly.get(Calendar.MINUTE))
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }
            } else {
                Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addSleepEntry(babyId.toInt(), startText, endText, notes)
        }
    }

    private fun showEditSleepDialog(entry: SleepEntry) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_sleep, null)
        val etStart = dialogView.findViewById<EditText>(R.id.etEditSleepStart)
        val etEnd = dialogView.findViewById<EditText>(R.id.etEditSleepEnd)
        val etNotes = dialogView.findViewById<EditText>(R.id.etEditSleepNotes)

        etStart.setText(entry.sleep_start)
        etEnd.setText(entry.sleep_end)
        etNotes.setText(entry.notes ?: "")

        AlertDialog.Builder(this)
            .setTitle("Edit Sleep Entry")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                viewModel.updateSleepEntry(
                    entry.sleep_id,
                    etStart.text.toString(),
                    etEnd.text.toString(),
                    etNotes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTimePicker(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                targetEditText.setText(formattedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }
}
