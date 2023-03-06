package ch.selimovic.scrumpro.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import ch.selimovic.scrumpro.domain.NewMeetingPresenter
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

        val presenter = NewMeetingPresenter(this, dbHelper)

        // Get the CalendarView object and set a OnDateChangeListener
        val calendarAddView = findViewById<CalendarView>(R.id.calendarAddView)
        calendarAddView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Get the selected date as a LocalDate object
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

            // Get the selected meeting type from the spinner
            val spMeetingType = findViewById<Spinner>(R.id.spMeetingType)
            val selectedMeetingType = spMeetingType.selectedItem.toString()

            // Create the new meeting
            presenter.createNewMeeting(selectedMeetingType, selectedDate)

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
