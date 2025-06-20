package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

class BabyDashboardFragment : Fragment() {

    private lateinit var viewModel: BabyViewModel
    private lateinit var babyAdapter: BabyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.activity_baby_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get userId from SharedPreferences
        userId = requireContext().getSharedPreferences("HeyMomPrefs", 0)
            .getString("user_id", "") ?: ""

        if (userId.isEmpty()) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return
        }

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerBabies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)
        val repository = BabyRepository(apiService)
        val factory = BabyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BabyViewModel::class.java]

        // Observe baby list
        viewModel.babies.observe(viewLifecycleOwner) { babyList ->
            babyAdapter = BabyAdapter(babyList) { selectedBaby ->
                val intent = Intent(requireContext(), RoutineMenuActivity::class.java)
                intent.putExtra("baby_id", selectedBaby.baby_id.toString())
                intent.putExtra("baby_name", selectedBaby.name)
                intent.putExtra("baby_dob", selectedBaby.dob)
                startActivity(intent)
            }
            recyclerView.adapter = babyAdapter
        }

        // Observe status
        viewModel.status.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        // Fetch babies
        viewModel.getBabies(userId.toInt())

        // FAB: Add baby
        view.findViewById<FloatingActionButton>(R.id.fabAddBaby).setOnClickListener {
            startActivity(Intent(requireContext(), AddBabyActivity::class.java))
        }
    }
}
