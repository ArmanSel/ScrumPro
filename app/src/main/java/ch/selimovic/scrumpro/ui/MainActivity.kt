package ch.selimovic.scrumpro.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import ch.selimovic.scrumpro.domain.MeetingPresenter
import ch.selimovic.scrumpro.domain.MeetingRepository
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var meetingPresenter: MeetingPresenter
    private lateinit var txtCalendarDetails: TextView

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val meetingDatabase = MeetingDatabase(this)
        val meetingRepository = MeetingRepository(meetingDatabase)
        meetingPresenter = MeetingPresenter(meetingRepository)

        txtCalendarDetails = findViewById(R.id.txtCalendarDetails)

        // Get the formatted meeting list from the presenter and display it in the TextView
        txtCalendarDetails.text = meetingPresenter.getFormattedMeetingList()

        // Set up button click listeners
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            val intent = Intent(this, NewMeetingActivity::class.java)
            startActivity(intent)
        }

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditMeetingActivity::class.java)
            startActivity(intent)
        }

        // Set up a PendingIntent to launch the app when the notification is clicked
        val intent = Intent(this, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        // Set up an AlarmManager to trigger the MeetingNotificationService every day at midnight
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val interval = AlarmManager.INTERVAL_DAY
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, interval, pendingIntent)
    }
}
