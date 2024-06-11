package com.uniza.wordlevel

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class NotificationHandler(private val context: Context) {
    private val notificationId = 0

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                "825",
                "wordlevel_notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "WordLevel Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun requestNotificationPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun showNotification(title: String, message: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        val notification = buildNotification(title, message)
        notificationManager.notify(notificationId, notification)
    }

    private fun buildNotification(title: String, message: String): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, "825")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.wordlevel_final)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun setupDailyNotification(notificationsEnabled: Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        if (notificationsEnabled) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        } else {
            alarmManager.cancel(alarmIntent)
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHandler = NotificationHandler(context)
        notificationHandler.showNotification("Daily reminder", "Don't forget to play WordLevel!")
    }
}
