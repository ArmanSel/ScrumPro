package ch.selimovic.scrumpro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var meetingDatabase: MeetingDatabase
    private lateinit var txtCalendarDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meetingDatabase = MeetingDatabase(this)
        txtCalendarDetails = findViewById(R.id.txtCalendarDetails)

        // Retrieve all meetings from the database
        val meetings = meetingDatabase.getAllMeetings()

        // Format the meetings as a string and display them in the TextView
        val meetingsString = meetings.joinToString(separator = "\n") { meeting ->
            "${meeting.type} - ${meeting.dateFormatted}"
        }
        txtCalendarDetails.text = meetingsString

        val btnCreate = findViewById<Button>(R.id.btnCreate)

        btnCreate.setOnClickListener {
            val intent = Intent(this, NewMeetingActivity::class.java)
            startActivity(intent)

        }

    }

}
