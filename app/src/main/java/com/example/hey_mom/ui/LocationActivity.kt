package com.example.hey_mom.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.databinding.ActivityLocationBinding
import com.example.hey_mom.repository.LocationRepository
import com.example.hey_mom.ui.adapters.LocationAdapter
import com.example.hey_mom.viewmodel.LocationViewModel
import com.example.hey_mom.viewmodel.LocationViewModelFactory

class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var clinicAdapter: LocationAdapter
    private lateinit var daycareAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel with Factory
        val api = ApiClient.retrofit.create(ApiService::class.java)
        val repo = LocationRepository(api)
        viewModel = ViewModelProvider(this, LocationViewModelFactory(repo))[LocationViewModel::class.java]

        // Setup RecyclerViews
        clinicAdapter = LocationAdapter { openMap(it.address) }
        daycareAdapter = LocationAdapter { openMap(it.address) }

        binding.recyclerClinics.layoutManager = LinearLayoutManager(this)
        binding.recyclerClinics.adapter = clinicAdapter

        binding.recyclerDaycares.layoutManager = LinearLayoutManager(this)
        binding.recyclerDaycares.adapter = daycareAdapter

        // Observe ViewModel
        viewModel.clinics.observe(this) {
            clinicAdapter.submitList(it)
        }

        viewModel.daycareCenters.observe(this) {
            daycareAdapter.submitList(it)
        }

        viewModel.status.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchLocations()
    }

    private fun openMap(address: String) {
        val uri = Uri.parse("google.navigation:q=${Uri.encode(address)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show()
        }
    }
}
