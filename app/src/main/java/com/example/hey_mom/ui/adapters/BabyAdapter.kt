package com.example.hey_mom.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.Baby
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BabyAdapter(
    private val babies: List<Baby>,
    private val onClick: (Baby) -> Unit
) : RecyclerView.Adapter<BabyAdapter.BabyViewHolder>() {

    inner class BabyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBabyName: TextView = itemView.findViewById(R.id.tvBabyName)
        private val tvBabyAge: TextView = itemView.findViewById(R.id.tvBabyAge)
        private val tvBabyDob: TextView = itemView.findViewById(R.id.tvBabyDob)
        private val tvBabyGender: TextView = itemView.findViewById(R.id.tvBabyGender)
        private val ivBabyAvatar: ImageView = itemView.findViewById(R.id.ivBabyAvatar)

        fun bind(baby: Baby) {
            // Set baby name
            tvBabyName.text = baby.name

            // Set baby gender
            tvBabyGender.text = baby.gender ?: "Not specified"

            // Format and set date of birth
            tvBabyDob.text = formatDateOfBirth(baby.dob)

            // Calculate and set age
            tvBabyAge.text = calculateAge(baby.dob)

            // Set avatar based on gender
            setAvatarByGender(baby.gender)

            // Set click listener
            itemView.setOnClickListener { onClick(baby) }

        }

        private fun formatDateOfBirth(dob: String?): String {
            if (dob.isNullOrEmpty()) return "Not specified"

            return try {
                // Assuming your API returns date in format "yyyy-MM-dd"
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(dob)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                // If parsing fails, return the original string
                dob
            }
        }

        private fun calculateAge(dob: String?): String {
            if (dob.isNullOrEmpty()) return "Age unknown"

            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val birthDate = inputFormat.parse(dob) ?: return "Age unknown"
                val currentDate = Date()

                val diffInMillis = currentDate.time - birthDate.time
                val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                when {
                    days < 30 -> "${days} days old"
                    days < 365 -> {
                        val months = days / 30
                        if (months == 1L) "1 month old" else "${months} months old"
                    }
                    else -> {
                        val years = days / 365
                        val remainingMonths = (days % 365) / 30
                        when {
                            years == 1L && remainingMonths == 0L -> "1 year old"
                            years == 1L -> "1 year, ${remainingMonths} months old"
                            remainingMonths == 0L -> "${years} years old"
                            else -> "${years} years, ${remainingMonths} months old"
                        }
                    }
                }
            } catch (e: Exception) {
                "Age unknown"
            }
        }

        private fun setAvatarByGender(gender: String?) {
            when (gender?.lowercase()) {
                "male", "boy" -> {
                    ivBabyAvatar.setImageResource(R.drawable.baseline_account_circle_24)
                    ivBabyAvatar.setBackgroundColor(itemView.context.getColor(R.color.purple2))

                }
                "female", "girl" -> {
                    ivBabyAvatar.setImageResource(R.drawable.baseline_account_circle_24)
                    ivBabyAvatar.setBackgroundColor(itemView.context.getColor(R.color.purple2))

                }
                else -> {
                    ivBabyAvatar.setImageResource(R.drawable.baseline_account_circle_24)
                    ivBabyAvatar.setBackgroundColor(itemView.context.getColor(R.color.purple2))
                }
            }
        }

        private fun showOptionsMenu(baby: Baby) {
            // TODO: Implement popup menu or bottom sheet
            // Example: Edit baby, Delete baby, View details, etc.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baby, parent, false) // Make sure this matches your layout name
        return BabyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        holder.bind(babies[position])
    }

    override fun getItemCount() = babies.size
}