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
import com.example.hey_mom.api.models.DiaperChange
import com.example.hey_mom.repository.DiaperRepository
import com.example.hey_mom.ui.adapters.DiaperAdapter
import com.example.hey_mom.viewmodel.DiaperViewModel
import com.example.hey_mom.viewmodel.DiaperViewModelFactory

class DiaperActivity : AppCompatActivity() {

    private lateinit var viewModel: DiaperViewModel
    private lateinit var babyId: String

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
            if (it == "success") viewModel.getDiaperEntries(babyId.toInt())
        }

        viewModel.getDiaperEntries(babyId.toInt())

        findViewById<Button>(R.id.btnAddDiaper).setOnClickListener {
            val time = findViewById<EditText>(R.id.etDiaperTime).text.toString()
            val condition = findViewById<EditText>(R.id.etCondition).text.toString()
            val notes = findViewById<EditText>(R.id.etDiaperNotes).text.toString()

            if (time.isEmpty() || condition.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addDiaperEntry(babyId.toInt(), time, condition, notes)
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

