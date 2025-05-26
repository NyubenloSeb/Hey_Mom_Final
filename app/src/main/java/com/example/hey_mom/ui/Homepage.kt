package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hey_mom.R
import com.example.hey_mom.databinding.ActivityHomepageBinding

class Homepage : AppCompatActivity() {
    lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(OverviewFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.overview -> replaceFragment(OverviewFragment())
                R.id.routine -> replaceFragment(RoutineFragment())
                R.id.child_details -> {
                    val intent = Intent(this, BabyDashboardActivity::class.java)
                    startActivity(intent)
                }
                R.id.profile -> replaceFragment(ProfileFragment())
            else ->{
            }
            }
            true
        }
    }
    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager= supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}

