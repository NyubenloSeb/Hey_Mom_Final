package com.example.hey_mom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.hey_mom.databinding.ActivityHomepageBinding
import com.example.hey_mom.databinding.ActivitySigninBinding

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
                R.id.child_details -> replaceFragment(ChildDetailsFragment())
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

