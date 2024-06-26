package com.android.mynotification.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mynotification.R
import com.android.mynotification.ui.adapter.Notification
import com.android.mynotification.ui.adapter.NotificationAdapter

class MainActivity : AppCompatActivity() {

    private val channelId = "your_channel_id"
    private val groupKey = "com.android.mynotification.GROUP_KEY"
    private val groupSummaryId = 1
    private val notificationId1 = 1
    private val notificationId2 = 2

    private lateinit var notificationManager: NotificationManagerCompat

    private lateinit var notifications: List<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createDummyNotifications()
        setupRecyclerView()

        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        sendGroupNotifications()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotificationAdapter(notifications)
    }

    private fun createDummyNotifications() {
        notifications = listOf(
            Notification(1, "Title 1", "Message 1", "2024-05-16 10:00"),
            Notification(2, "Title 2", "Message 2", "2024-05-16 11:00"),
            // Add more notifications here
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Channel for grouped notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendGroupNotifications() {

        val pendingIntent1: PendingIntent =
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent2: PendingIntent =
            PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent3: PendingIntent =
            PendingIntent.getActivity(this, 3, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification1 = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Message from Alice")
            .setContentText("Hey, how are you?")
            .setSmallIcon(R.drawable.ic_notification)
            //.setContentIntent(pendingIntent1)
            //.setAutoCancel(true)
            .setGroup(groupKey)
            .build()

        val notification2 = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Message from Bob")
            .setContentText("Are we still meeting today?")
            .setSmallIcon(R.drawable.ic_notification)
            //.setContentIntent(pendingIntent2)
            //.setAutoCancel(true)
            .setGroup(groupKey)
            .build()

        val summaryNotification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("You have new messages")
            .setContentText("Expand to see the messages")
            .setSmallIcon(R.drawable.ic_notification)
            //.setContentIntent(pendingIntent3)
            //.setAutoCancel(true)
            .setGroup(groupKey)
            .setGroupSummary(true)
            .build()

        with(notificationManager) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            notify(notificationId1, notification1)
            notify(notificationId2, notification2)
            //notify(groupSummaryId, summaryNotification)
        }
    }
}
