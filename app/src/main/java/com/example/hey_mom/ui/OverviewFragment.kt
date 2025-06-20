package com.example.hey_mom.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.DiaperChange
import com.example.hey_mom.api.models.FeedingEntry
import com.example.hey_mom.api.models.SleepEntry
import com.example.hey_mom.notifications.NotificationScheduler
import com.example.hey_mom.repository.BabyRepository
import com.example.hey_mom.repository.DiaperRepository
import com.example.hey_mom.repository.FeedingRepository
import com.example.hey_mom.repository.SleepRepository
import com.example.hey_mom.ui.adapters.DiaperAdapter
import com.example.hey_mom.ui.adapters.FeedingAdapter
import com.example.hey_mom.ui.adapters.ProfileAdapter
import com.example.hey_mom.ui.adapters.SleepAdapter
import com.example.hey_mom.viewmodel.BabyViewModel
import com.example.hey_mom.viewmodel.BabyViewModelFactory
import com.example.hey_mom.viewmodel.DiaperViewModel
import com.example.hey_mom.viewmodel.DiaperViewModelFactory
import com.example.hey_mom.viewmodel.FeedingViewModel
import com.example.hey_mom.viewmodel.FeedingViewModelFactory
import com.example.hey_mom.viewmodel.SleepViewModel
import com.example.hey_mom.viewmodel.SleepViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import java.util.Calendar
import java.util.Random

class OverviewFragment : Fragment() {

    private lateinit var babyViewModel: BabyViewModel
    private lateinit var feedingViewModel: FeedingViewModel
    private lateinit var diaperViewModel: DiaperViewModel
    private lateinit var sleepViewModel: SleepViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var feedingAdapter: FeedingAdapter
    private lateinit var diaperAdapter: DiaperAdapter
    private lateinit var sleepAdapter: SleepAdapter
    private lateinit var feedingRecycler: RecyclerView
    private lateinit var diaperRecycler: RecyclerView
    private lateinit var sleepRecycler: RecyclerView
    private lateinit var navView: NavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var userId: String
    private lateinit var profileRecycler: RecyclerView
    private var selectedBabyId: String? = null
    private var selectedCalendar: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize userId
        userId = requireContext().getSharedPreferences("HeyMomPrefs", 0)
            .getString("user_id", "") ?: ""

        setupViewModels()
        setupViews(view)
        setupObservers()
        setupStatusBar()
        setupDrawer(view)

        // Fetch babies initially
        babyViewModel.getBabies(userId.toInt())
    }

    private fun setupViewModels() {
        val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

        // Baby ViewModel
        val babyRepository = BabyRepository(apiService)
        val babyFactory = BabyViewModelFactory(babyRepository)
        babyViewModel = ViewModelProvider(this, babyFactory)[BabyViewModel::class.java]

        // Feeding ViewModel
        val feedingRepository = FeedingRepository(apiService)
        val feedingFactory = FeedingViewModelFactory(feedingRepository)
        feedingViewModel = ViewModelProvider(this, feedingFactory)[FeedingViewModel::class.java]

        // Diaper ViewModel
        val diaperRepository = DiaperRepository(apiService)
        val diaperFactory = DiaperViewModelFactory(diaperRepository)
        diaperViewModel = ViewModelProvider(this, diaperFactory)[DiaperViewModel::class.java]

        // Sleep ViewModel
        val sleepRepository = SleepRepository(apiService)
        val sleepFactory = SleepViewModelFactory(sleepRepository)
        sleepViewModel = ViewModelProvider(this, sleepFactory)[SleepViewModel::class.java]
    }

    private fun setupViews(view: View) {
        // Profile RecyclerView (horizontal)
        profileRecycler = view.findViewById<RecyclerView>(R.id.profileRecycler)
        profileRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Feeding RecyclerView (vertical)
        feedingRecycler = view.findViewById<RecyclerView>(R.id.viewfeed)
        feedingRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Diaper RecyclerView (vertical)
        diaperRecycler = view.findViewById<RecyclerView>(R.id.viewdiaper)
        diaperRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Sleep RecyclerView (vertical)
        sleepRecycler = view.findViewById<RecyclerView>(R.id.viewsleep)
        sleepRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapters
        feedingAdapter = FeedingAdapter(emptyList()) { entry ->
            showEditFeedingDialog(entry)
        }
        feedingRecycler.adapter = feedingAdapter

        diaperAdapter = DiaperAdapter(emptyList()) { entry ->
            showEditDiaperDialog(entry)
        }
        diaperRecycler.adapter = diaperAdapter

        sleepAdapter = SleepAdapter(emptyList()) { entry ->
            showEditSleepDialog(entry)
        }
        sleepRecycler.adapter = sleepAdapter
    }

    private fun setupObservers() {
        // Observe baby list
        babyViewModel.babies.observe(viewLifecycleOwner) { babyList ->
            profileAdapter = ProfileAdapter(babyList) { selectedBaby ->
                // Update selected baby and load all routine data
                selectedBabyId = selectedBaby.baby_id.toString()
                loadAllRoutineData(selectedBaby.baby_id)

                // Navigate to routine menu
                val intent = Intent(requireContext(), RoutineMenuActivity::class.java)
                intent.putExtra("baby_id", selectedBaby.baby_id.toString())
                intent.putExtra("baby_name", selectedBaby.name)
            }
            profileRecycler.adapter = profileAdapter

            // Auto-select first baby if available
            if (babyList.isNotEmpty() && selectedBabyId == null) {
                selectedBabyId = babyList[0].baby_id.toString()
                loadAllRoutineData(babyList[0].baby_id)
            }
        }

        // Observe feeding list
        feedingViewModel.feedingList.observe(viewLifecycleOwner) { feedingList ->
            feedingAdapter = FeedingAdapter(feedingList) { entry ->
                showEditFeedingDialog(entry)
            }
            feedingRecycler.adapter = feedingAdapter
        }

        // Observe diaper list
        diaperViewModel.diaperList.observe(viewLifecycleOwner) { diaperList ->
            diaperAdapter = DiaperAdapter(diaperList) { entry ->
                showEditDiaperDialog(entry)
            }
            diaperRecycler.adapter = diaperAdapter
        }

        // Observe sleep list
        sleepViewModel.sleepList.observe(viewLifecycleOwner) { sleepList ->
            sleepAdapter = SleepAdapter(sleepList) { entry ->
                showEditSleepDialog(entry)
            }
            sleepRecycler.adapter = sleepAdapter
        }

        // Observe baby status
        babyViewModel.status.observe(viewLifecycleOwner) { status ->
            if (!status.isNullOrEmpty()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe feeding status
        feedingViewModel.status.observe(viewLifecycleOwner) { status ->
            if (!status.isNullOrEmpty()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                if (status.equals("success", ignoreCase = true)) {
                    selectedBabyId?.let { babyId ->
                        feedingViewModel.getFeedingEntries(babyId.toInt())
                    }

                    // Schedule notification if calendar is set
                    selectedCalendar?.let { calendar ->
                        NotificationScheduler.scheduleOneTimeAlarm(
                            context = requireContext(),
                            id = Random().nextInt(10000),
                            triggerAtMillis = calendar.timeInMillis,
                            type = "Feeding"
                        )
                        Toast.makeText(requireContext(), "Feeding reminder scheduled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Observe diaper status
        diaperViewModel.status.observe(viewLifecycleOwner) { status ->
            if (!status.isNullOrEmpty()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                if (status.equals("success", ignoreCase = true)) {
                    selectedBabyId?.let { babyId ->
                        diaperViewModel.getDiaperEntries(babyId.toInt())
                    }

                    // Schedule notification if calendar is set
                    selectedCalendar?.let { calendar ->
                        NotificationScheduler.scheduleOneTimeAlarm(
                            context = requireContext(),
                            id = Random().nextInt(10000),
                            triggerAtMillis = calendar.timeInMillis,
                            type = "Diaper"
                        )
                        Toast.makeText(requireContext(), "Diaper reminder scheduled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Observe sleep status
        sleepViewModel.status.observe(viewLifecycleOwner) { status ->
            if (!status.isNullOrEmpty()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                if (status.equals("success", ignoreCase = true)) {
                    selectedBabyId?.let { babyId ->
                        sleepViewModel.getSleepEntries(babyId.toInt())
                    }

                    // Schedule notification if calendar is set
                    selectedCalendar?.let { calendar ->
                        NotificationScheduler.scheduleOneTimeAlarm(
                            context = requireContext(),
                            id = Random().nextInt(10000),
                            triggerAtMillis = calendar.timeInMillis,
                            type = "Sleep"
                        )
                        Toast.makeText(requireContext(), "Sleep reminder scheduled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadAllRoutineData(babyId: Int) {
        feedingViewModel.getFeedingEntries(babyId)
        diaperViewModel.getDiaperEntries(babyId)
        sleepViewModel.getSleepEntries(babyId)
    }

    private fun showEditFeedingDialog(entry: FeedingEntry) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_feeding, null)
        val etTime = dialogView.findViewById<EditText>(R.id.etEditFeedingTime)
        val etType = dialogView.findViewById<EditText>(R.id.etEditFeedingType)
        val etQty = dialogView.findViewById<EditText>(R.id.etEditQuantity)
        val etNotes = dialogView.findViewById<EditText>(R.id.etEditFeedingNotes)

        etTime.setText(entry.feeding_time)
        etType.setText(entry.feeding_type)
        etQty.setText(entry.quantity_ml.toString())
        etNotes.setText(entry.notes ?: "")

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Feeding Entry")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                feedingViewModel.updateFeedingEntry(
                    entry.feeding_id,
                    etTime.text.toString(),
                    etType.text.toString(),
                    etQty.text.toString().toIntOrNull() ?: 0,
                    etNotes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
    }
        private fun showEditDiaperDialog(entry: DiaperChange) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_diaper, null)
            val etTime = dialogView.findViewById<EditText>(R.id.etEditDiaperTime)
            val etCondition = dialogView.findViewById<EditText>(R.id.etEditCondition)
            val etNotes = dialogView.findViewById<EditText>(R.id.etEditDiaperNotes)

            etTime.setText(entry.change_time)
            etCondition.setText(entry.condition)
            etNotes.setText(entry.notes ?: "")

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Diaper Entry")
                .setView(dialogView)
                .setPositiveButton("Update") { _, _ ->
                    diaperViewModel.updateDiaperEntry(
                        entry.diaper_ch_id,
                        etTime.text.toString(),
                        etCondition.text.toString(),
                        etNotes.text.toString()
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun showEditSleepDialog(entry: SleepEntry) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_sleep, null)
            val etStart = dialogView.findViewById<EditText>(R.id.etEditSleepStart)
            val etEnd = dialogView.findViewById<EditText>(R.id.etEditSleepEnd)
            val etNotes = dialogView.findViewById<EditText>(R.id.etEditSleepNotes)

            etStart.setText(entry.sleep_start)
            etEnd.setText(entry.sleep_end)
            etNotes.setText(entry.notes ?: "")

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Sleep Entry")
                .setView(dialogView)
                .setPositiveButton("Update") { _, _ ->
                    sleepViewModel.updateSleepEntry(
                        entry.sleep_id,
                        etStart.text.toString(),
                        etEnd.text.toString(),
                        etNotes.text.toString()
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun setupStatusBar() {
            val window = requireActivity().window
            window.statusBarColor = Color.WHITE
            setLightStatusBar(window, isLight = true)
        }

        private fun setupDrawer(view: View) {
            drawerLayout = view.findViewById(R.id.drawerLayout)
            navView = view.findViewById(R.id.navigationView)
            toolbar = view.findViewById(R.id.topAppBar)

            // Set toolbar
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

            toggle = ActionBarDrawerToggle(
                activity,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            // Handle navigation item clicks
            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.vaccine -> {
                        val intent = Intent(requireContext(), Vaccine_Activity::class.java)
                        startActivity(intent)
                    }
                    R.id.emergency -> {
                        val intent = Intent(requireContext(), emergency::class.java)
                        startActivity(intent)
                    }

                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }


    private fun setLightStatusBar(window: Window, isLight: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = window.insetsController
                if (controller != null) {
                    if (isLight) {
                        controller.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        controller.setSystemBarsAppearance(
                            0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    if (isLight) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
            }
        }

        // Optional: Add method to refresh all routine data when fragment becomes visible
        override fun onResume() {
            super.onResume()
            selectedBabyId?.let { babyId ->
                loadAllRoutineData(babyId.toInt())
            }
        }
    }