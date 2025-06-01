package com.example.hey_mom.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.DiaperChange
import com.example.hey_mom.notifications.NotificationScheduler
import com.example.hey_mom.repository.DiaperRepository
import com.example.hey_mom.ui.adapters.DiaperAdapter
import com.example.hey_mom.viewmodel.DiaperViewModel
import com.example.hey_mom.viewmodel.DiaperViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DiaperActivity : AppCompatActivity() {

    private lateinit var viewModel: DiaperViewModel
    private lateinit var babyId: String
    private var selectedCalendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diaper)

        babyId = intent.getStringExtra("baby_id") ?: ""
        val api = ApiClient.retrofit.create(ApiService::class.java)
        val factory = DiaperViewModelFactory(DiaperRepository(api))
        viewModel = ViewModelProvider(this, factory)[DiaperViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerDiaper)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.diaperList.observe(this) {
            recycler.adapter = DiaperAdapter(it, onEdit = { entry -> showEditDiaperDialog(entry) })
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == "success") {
                viewModel.getDiaperEntries(babyId.toInt())
                selectedCalendar?.let { calendar ->
                    NotificationScheduler.scheduleOneTimeAlarm(
                        context = this,
                        id = Random().nextInt(10000),
                        triggerAtMillis = calendar.timeInMillis,
                        type = "Diaper"
                    )
                    Toast.makeText(this, "Diaper reminder scheduled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getDiaperEntries(babyId.toInt())

        val timeInput = findViewById<EditText>(R.id.etDiaperTime)
        val conditionInput = findViewById<EditText>(R.id.etCondition)
        val notesInput = findViewById<EditText>(R.id.etDiaperNotes)

        findViewById<Button>(R.id.btnAddDiaper).setOnClickListener {
            val timeText = timeInput.text.toString().trim()
            val condition = conditionInput.text.toString().trim()
            val notes = notesInput.text.toString()

            if (timeText.isEmpty() || condition.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val parsedDate = sdf.parse(timeText)

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

            viewModel.addDiaperEntry(babyId.toInt(), timeText, condition, notes)
        }

        // Show time picker when clicking on time field
        timeInput.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                val formattedTime = String.format("%02d:%02d", hour, minute)
                timeInput.setText(formattedTime)
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }
    }

    private fun showEditDiaperDialog(entry: DiaperChange) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_diaper, null)
        val etTime = dialogView.findViewById<EditText>(R.id.etEditDiaperTime)
        val etCondition = dialogView.findViewById<EditText>(R.id.etEditCondition)
        val etNotes = dialogView.findViewById<EditText>(R.id.etEditDiaperNotes)

        etTime.setText(entry.change_time)
        etCondition.setText(entry.condition)
        etNotes.setText(entry.notes ?: "")

        AlertDialog.Builder(this)
            .setTitle("Edit Diaper Entry")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                viewModel.updateDiaperEntry(
                    entry.diaper_ch_id,
                    etTime.text.toString(),
                    etCondition.text.toString(),
                    etNotes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
