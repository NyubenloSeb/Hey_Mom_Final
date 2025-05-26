package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.repository.BabyRepository
import com.example.hey_mom.ui.adapters.BabyAdapter
import com.example.hey_mom.viewmodel.BabyViewModel
import com.example.hey_mom.viewmodel.BabyViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BabyDashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyViewModel
    private lateinit var babyAdapter: BabyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userId: String // Retrieved from SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_dashboard)

        // Get userId from SharedPreferences
        userId = getSharedPreferences("HeyMomPrefs", MODE_PRIVATE)
            .getString("user_id", "") ?: ""

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerBabies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ApiService, Repository, and ViewModel using Factory
        val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)
        val repository = BabyRepository(apiService)
        val factory = BabyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BabyViewModel::class.java]

        // Observe baby list and bind to RecyclerView
        viewModel.babies.observe(this) { babyList ->
            babyAdapter = BabyAdapter(babyList) { selectedBaby ->
                val intent = Intent(this, RoutineMenuActivity::class.java)
                intent.putExtra("baby_id", selectedBaby.baby_id.toString())
                startActivity(intent)
            }
            recyclerView.adapter = babyAdapter
        }

        // Observe status messages
        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        // Fetch babies from backend
        viewModel.getBabies(userId.toInt())

        // Handle FAB to add a new baby
        findViewById<FloatingActionButton>(R.id.fabAddBaby).setOnClickListener {
            startActivity(Intent(this, AddBabyActivity::class.java))
        }
    }
}
