package com.example.hey_mom.notifications


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hey_mom.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_FEEDING   = "feeding_reminders"
        const val CHANNEL_SLEEP     = "sleep_reminders"
        const val CHANNEL_DIAPER    = "diaper_reminders"
        const val CHANNEL_VACCINE   = "vaccine_reminders"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Create notification channels on app startup
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Define channel name and importance for each reminder type
            val feedingChannel = NotificationChannel(
                CHANNEL_FEEDING, "Feeding Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for baby feeding times"
            }
            val sleepChannel = NotificationChannel(
                CHANNEL_SLEEP, "Sleep Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for baby sleep times"
            }
            val diaperChannel = NotificationChannel(
                CHANNEL_DIAPER, "Diaper Change", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for diaper changes"
            }
            val vaccineChannel = NotificationChannel(
                CHANNEL_VACCINE, "Vaccination Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for upcoming vaccinations"
            }
            // Register channels with the system (no-op on older devices)
            notificationManager.createNotificationChannel(feedingChannel)
            notificationManager.createNotificationChannel(sleepChannel)
            notificationManager.createNotificationChannel(diaperChannel)
            notificationManager.createNotificationChannel(vaccineChannel)
        }
    }

    /** Send a notification on the given [channelId] with [title] and [message]. */
    fun sendNotification(channelId: String, title: String, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.child)   //
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}
