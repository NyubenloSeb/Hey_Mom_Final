package com.example.hey_mom

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

data class GridItem(
    val title: String,
    val iconRes: Int
)
class RoutineFragment : Fragment() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_routine, container, false)


        val gridLayout = view.findViewById<GridLayout>(R.id.gridLayout)

        val itemList = listOf(
            GridItem("Sleep", R.drawable.babysleep),
            GridItem("Meal", R.drawable.bottle),
            GridItem("Sleep", R.drawable.babysleep),
            GridItem("Diaper", R.drawable.diaper)
        )

        for (item in itemList) {
            val itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.grid_item, gridLayout, false)

            val textView = itemView.findViewById<TextView>(R.id.textView)
            val imageView = itemView.findViewById<ImageView>(R.id.item_image)

            textView.text = item.title
            imageView.setImageResource(item.iconRes)

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(16, 16, 16, 16)
            }

            gridLayout.addView(itemView, params)
        }


        return view
    }

}




