package com.example.hey_mom.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.repository.GrowthRepository
import com.example.hey_mom.ui.adapters.GrowthAdapter
import com.example.hey_mom.viewmodel.GrowthViewModel
import com.example.hey_mom.viewmodel.GrowthViewModelFactory

class GrowthTrackingActivity : AppCompatActivity() {

    private lateinit var viewModel: GrowthViewModel
    private lateinit var babyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growth_tracking)

        babyId = intent.getStringExtra("baby_id") ?: ""
        val api = ApiClient.retrofit.create(ApiService::class.java)
        val factory = GrowthViewModelFactory(GrowthRepository(api))
        viewModel = ViewModelProvider(this, factory)[GrowthViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerGrowth)
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel.growthList.observe(this) {
            recycler.adapter = GrowthAdapter(it)
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == "success") viewModel.getGrowthEntries(babyId.toInt())
        }

        viewModel.getGrowthEntries(babyId.toInt())

        findViewById<Button>(R.id.btnAddGrowth).setOnClickListener {
            val date = findViewById<EditText>(R.id.etGrowthDate).text.toString()
            val weight = findViewById<EditText>(R.id.etWeight).text.toString().toFloatOrNull() ?: 0f
            val height = findViewById<EditText>(R.id.etHeight).text.toString().toFloatOrNull() ?: 0f
            val head = findViewById<EditText>(R.id.etHeadCircumference).text.toString().toFloatOrNull() ?: 0f
            val notes = findViewById<EditText>(R.id.etGrowthNotes).text.toString()

            if (date.isEmpty()) {
                Toast.makeText(this, "Please enter the date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addGrowthEntry(babyId.toInt(), date, weight, height, head, notes)
        }
    }
}
