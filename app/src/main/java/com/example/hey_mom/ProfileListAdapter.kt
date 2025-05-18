package com.example.hey_mom

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.widget.ArrayAdapter
import android.view.ViewGroup

data class ProfileItem(
    val name: String,
    val iconResId: Int,
    val iconResId2: Int)


class ProfileListAdapter(
    private val context: Context,
    private val profile: List<ProfileItem>
) : ArrayAdapter<ProfileItem>(context, 0, profile) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.profile_list, parent, false)
        val child = profile[position]

        val nameTextView = view.findViewById<TextView>(R.id.profiletext)
        val iconImageView = view.findViewById<ImageView>(R.id.profileIcon1)
        val iconImageView2 = view.findViewById<ImageView>(R.id.profileIcon2)

        nameTextView.text = child.name
        iconImageView.setImageResource(child.iconResId)
        iconImageView2.setImageResource(child.iconResId2)

        return view
    }
}
