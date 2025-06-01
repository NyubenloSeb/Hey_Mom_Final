package com.example.hey_mom.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Extract type and ID from the alarm intent
        val type = intent.getStringExtra("NOTIF_TYPE") ?: "Unknown"
        val notifId = intent.getIntExtra("NOTIF_ID", 0)

        // Decide title, message, and channel based on type
        val (title, message, channelId) = when (type) {
            "Feeding" -> Triple(
                "Time to Feed",
                "It's feeding time for your baby!",
                NotificationHelper.CHANNEL_FEEDING
            )
            "Sleep" -> Triple(
                "Time to Sleep",
                "It's nap time for your baby!",
                NotificationHelper.CHANNEL_SLEEP
            )
            "Diaper" -> Triple(
                "Diaper Change",
                "Time to change the diaper.",
                NotificationHelper.CHANNEL_DIAPER
            )
            "Vaccination" -> Triple(
                "Vaccination Reminder",
                "Don't forget the upcoming vaccination date!",
                NotificationHelper.CHANNEL_VACCINE
            )
            else -> Triple(
                "Hey Mom",
                "You have a scheduled reminder.",
                NotificationHelper.CHANNEL_FEEDING
            )
        }

        // Send the notification using NotificationHelper
        NotificationHelper(context).sendNotification(channelId, title, message, notifId)
    }
}
