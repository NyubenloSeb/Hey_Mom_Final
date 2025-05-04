package com.example.hey_mom

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import kotlin.math.abs



class OverviewFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //for the icons
        val profileRecycler = view.findViewById<RecyclerView>(R.id.profileRecycler)

        val profileIcons = listOf(
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,
            R.drawable.baseline_account_circle_24,


        )

        val profileAdapter = ProfileAdapter(profileIcons)

        profileRecycler.adapter = profileAdapter
        profileRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // for display cards
        childFragmentManager.beginTransaction()
            .replace(R.id.display, Routinedisplay())
            .commit()

        //for status bar
        val window = requireActivity().window
        window.statusBarColor = Color.WHITE // or ContextCompat.getColor(requireContext(), R.color.your_bg)
        setLightStatusBar(window, isLight = true)


        //for drawer
        drawerLayout = view.findViewById(R.id.drawerLayout)
        navView = view.findViewById(R.id.navigationView)
        toolbar = view.findViewById(R.id.topAppBar)

        // If you're using Toolbar in a Fragment, you must set it manually
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
                R.id.location -> {
                    val intent = Intent(requireContext(), LocationActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        //Card style infinite view
        viewPager = view.findViewById(R.id.viewPager)

        val imageList = listOf(
            R.drawable.card1,
            R.drawable.card2,
            R.drawable.card3
        )

        val titleList = listOf(
            "Recommended App",
            "Top Rated",
            "Editor's Pick"
        )

        // Set the adapter
        viewPager.adapter = CardAdapter(imageList, titleList)

        // Set the starting point to a large number to simulate infinite scroll
        val startPosition = Int.MAX_VALUE / 2
        viewPager.setCurrentItem(startPosition, false)

        // Setting the page margin and offset for smooth transitions
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset)

        // ViewPager settings
        viewPager.offscreenPageLimit = 3

        // Apply scale and translation transformation for pop-up effect
        viewPager.setPageTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f  // Scale between 0.85 and 1

            // Horizontal translation based on position
            val translationX = position * -pageOffset

            // Reduce the vertical translation to keep cards within the screen bounds
            val translationY = 0f  // Prevents the card from moving vertically too much

            // Ensure alpha does not go too low (keep it above 0.5 for visibility)
            val alphaValue = 1 - abs(position) * 0.5f  // Reduce alpha but keep it visible

            // Apply transformations
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.translationX = translationX
            page.translationY = translationY
            page.alpha = alphaValue  // Ensure the off-center cards are still visible
        }

    }

    //for status bar
    fun setLightStatusBar(window: Window, isLight: Boolean) {
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

}

