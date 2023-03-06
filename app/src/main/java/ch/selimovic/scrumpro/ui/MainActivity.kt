package ch.selimovic.scrumpro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import ch.selimovic.scrumpro.domain.MeetingPresenter
import ch.selimovic.scrumpro.domain.MeetingRepository

class MainActivity : AppCompatActivity() {

    private lateinit var meetingPresenter: MeetingPresenter
    private lateinit var txtCalendarDetails: TextView

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
    }
}
