package ch.selimovic.scrumpro.ui

import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewMeetingActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newmeeting)

        // Get an instance of the MeetingDatabase
        val dbHelper = MeetingDatabase(this)
        val db = dbHelper.writableDatabase

        val btnApprove = findViewById<Button>(R.id.btnApprove)
        btnApprove.setOnClickListener {
            // Get the selected date as a Date object
            val calendarAddView = findViewById<CalendarView>(R.id.calendarAddView)
            val selectedDate = Date(calendarAddView.date)

            // Insert a new row into the meetings table
            val values = ContentValues().apply {
                put(MeetingDatabase.COLUMN_TYPE, "Daily Standup")
                put(MeetingDatabase.COLUMN_DATE, selectedDate.time)
            }
            val newRowId = db.insert(MeetingDatabase.TABLE_NAME, null, values)

            // Show a toast message to indicate success
            Toast.makeText(this, "Meeting created", Toast.LENGTH_SHORT).show()

            // Finish the activity to return to the previous screen
            finish()
        }
    }
}
