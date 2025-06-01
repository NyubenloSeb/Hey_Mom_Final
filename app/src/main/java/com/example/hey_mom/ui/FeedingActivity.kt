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
import com.example.hey_mom.api.models.FeedingEntry
import com.example.hey_mom.notifications.NotificationScheduler
import com.example.hey_mom.repository.FeedingRepository
import com.example.hey_mom.ui.adapters.FeedingAdapter
import com.example.hey_mom.viewmodel.FeedingViewModel
import com.example.hey_mom.viewmodel.FeedingViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class FeedingActivity : AppCompatActivity() {

    private lateinit var viewModel: FeedingViewModel
    private lateinit var babyId: String
    private lateinit var recycler: RecyclerView
    private var selectedCalendar: Calendar? = null // Used for alarm scheduling

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeding)

        babyId = intent.getStringExtra("baby_id") ?: ""

        // ViewModel setup
        val api: ApiService = ApiClient.retrofit.create(ApiService::class.java)
        val repository = FeedingRepository(api)
        val factory = FeedingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FeedingViewModel::class.java]

        recycler = findViewById(R.id.recyclerFeeding)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.feedingList.observe(this) {
            recycler.adapter = FeedingAdapter(it, onEdit = { entry -> showEditFeedingDialog(entry) })
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it.equals("success", ignoreCase = true)) {
                viewModel.getFeedingEntries(babyId.toInt())
                selectedCalendar?.let { calendar ->
                    NotificationScheduler.scheduleOneTimeAlarm(
                        context = this,
                        id = Random().nextInt(10000),
                        triggerAtMillis = calendar.timeInMillis,
                        type = "Feeding"
                    )
                    Toast.makeText(this, "Feeding reminder scheduled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getFeedingEntries(babyId.toInt())

        val timeInput = findViewById<EditText>(R.id.etFeedingTime)
        val typeInput = findViewById<EditText>(R.id.etFeedingType)
        val qtyInput = findViewById<EditText>(R.id.etQuantity)
        val notesInput = findViewById<EditText>(R.id.etFeedingNotes)

        findViewById<Button>(R.id.btnAddFeeding).setOnClickListener {
            val timeText = timeInput.text.toString().trim()  // renamed to avoid conflict
            val type = typeInput.text.toString().trim()
            val qty = qtyInput.text.toString().toIntOrNull() ?: 0
            val notes = notesInput.text.toString()

            if (timeText.isEmpty() || type.isEmpty()) {
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

            viewModel.addFeedingEntry(babyId.toInt(), timeText, type, qty, notes)
        }

        // Optional: show time picker
        timeInput.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                val formattedTime = String.format("%02d:%02d", hour, minute)
                timeInput.setText(formattedTime)
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }
    }

    private fun showEditFeedingDialog(entry: FeedingEntry) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_feeding, null)
        val etTime = dialogView.findViewById<EditText>(R.id.etEditFeedingTime)
        val etType = dialogView.findViewById<EditText>(R.id.etEditFeedingType)
        val etQty = dialogView.findViewById<EditText>(R.id.etEditQuantity)
        val etNotes = dialogView.findViewById<EditText>(R.id.etEditFeedingNotes)

        etTime.setText(entry.feeding_time)
        etType.setText(entry.feeding_type)
        etQty.setText(entry.quantity_ml.toString())
        etNotes.setText(entry.notes ?: "")

        AlertDialog.Builder(this)
            .setTitle("Edit Feeding Entry")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                viewModel.updateFeedingEntry(
                    entry.feeding_id,
                    etTime.text.toString(),
                    etType.text.toString(),
                    etQty.text.toString().toIntOrNull() ?: 0,
                    etNotes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
