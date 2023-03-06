package ch.selimovic.scrumpro.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewMeetingActivity : Activity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newmeeting)

        // Get an instance of the MeetingDatabase
        val dbHelper = MeetingDatabase(this)
        val db = dbHelper.writableDatabase

        // Get the CalendarView object and set a OnDateChangeListener
        val calendarAddView = findViewById<CalendarView>(R.id.calendarAddView)
        calendarAddView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Get the selected date as a Date object
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormatter.format(selectedDate.time)

            // Get the selected meeting type from the spinner
            val spMeetingType = findViewById<Spinner>(R.id.spMeetingType)
            val selectedMeetingType = spMeetingType.selectedItem.toString()

            // Insert a new row into the meetings table
            val values = ContentValues().apply {
                put(MeetingDatabase.COLUMN_TYPE, selectedMeetingType)
                put(MeetingDatabase.COLUMN_DATE, formattedDate)
            }
            val newRowId = db.insert(MeetingDatabase.TABLE_NAME, null, values)

            // Show a toast message to indicate success
            Toast.makeText(this, "Meeting created", Toast.LENGTH_SHORT).show()

            // Finish the activity to return to the previous screen
            finish()
        }

        val btnApprove = findViewById<Button>(R.id.btnApprove)
        btnApprove.setOnClickListener {
            // Show a toast message to inform the user to select a date first
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
        }
    }
}
