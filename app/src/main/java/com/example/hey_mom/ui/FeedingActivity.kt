package com.example.hey_mom.ui

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
import com.example.hey_mom.api.models.FeedingEntry
import com.example.hey_mom.repository.FeedingRepository
import com.example.hey_mom.ui.adapters.FeedingAdapter
import com.example.hey_mom.viewmodel.FeedingViewModel
import com.example.hey_mom.viewmodel.FeedingViewModelFactory

class FeedingActivity : AppCompatActivity() {

    private lateinit var viewModel: FeedingViewModel
    private lateinit var babyId: String
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeding)

        babyId = intent.getStringExtra("baby_id") ?: ""

        // Initialize ViewModel using custom factory
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
            }
        }

        viewModel.getFeedingEntries(babyId.toInt())

        findViewById<Button>(R.id.btnAddFeeding).setOnClickListener {
            val time = findViewById<EditText>(R.id.etFeedingTime).text.toString()
            val type = findViewById<EditText>(R.id.etFeedingType).text.toString()
            val qty = findViewById<EditText>(R.id.etQuantity).text.toString().toIntOrNull() ?: 0
            val notes = findViewById<EditText>(R.id.etFeedingNotes).text.toString()

            if (time.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addFeedingEntry(babyId.toInt(), time, type, qty, notes)
        }
    }

    private fun showEditFeedingDialog(entry: FeedingEntry) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_feeding, null)
        val etTime = dialogView.findViewById<EditText>(R.id.etEditFeedingTime)
        val etType = dialogView.findViewById<EditText>(R.id.etEditFeedingType)
        val etQty = dialogView.findViewById<EditText>(R.id.etEditQuantity)
        val etNotes = dialogView.findViewById<EditText>(R.id.etEditFeedingNotes)

        etTime.setText(entry.feeding_time)
        etType.setText(entry.feeding_type_notes)
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
