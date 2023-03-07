package ch.selimovic.scrumpro.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.Meeting
import ch.selimovic.scrumpro.data.MeetingDatabase
import ch.selimovic.scrumpro.domain.MeetingRepository
import ch.selimovic.scrumpro.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class MeetingNotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkForMeetingsToday()
        return START_STICKY
    }

    private fun checkForMeetingsToday() {
        val repository = MeetingRepository(MeetingDatabase.getInstance(applicationContext))
        val meetings = repository.getAllMeetings()
        val today = Calendar.getInstance().time

        meetings.filter { meeting ->
            meeting.dateFormatted == SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)
        }.forEach { meeting ->
            sendMeetingNotification(meeting)
        }
    }

    private fun sendMeetingNotification(meeting: Meeting) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "meeting")
            .setContentTitle("Meeting Today")
            .setContentText("You have a ${meeting.type} meeting today!")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
