package com.example.hey_mom

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.widget.ArrayAdapter
import android.view.ViewGroup

data class ChildItem(
    val name: String,
    val iconResId: Int)


class ListAdapter(
    private val context: Context,
    private val children: List<ChildItem>
) : ArrayAdapter<ChildItem>(context, 0, children) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_child, parent, false)
        val child = children[position]

        val nameTextView = view.findViewById<TextView>(R.id.tvChildName)
        val iconImageView = view.findViewById<ImageView>(R.id.ivChildIcon)

        nameTextView.text = child.name
        iconImageView.setImageResource(child.iconResId)

        return view
    }
}
