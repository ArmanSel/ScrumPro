package ch.selimovic.scrumpro.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.view.View
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ch.selimovic.scrumpro.R
import ch.selimovic.scrumpro.data.MeetingDatabase
import java.text.SimpleDateFormat
import java.util.*

class EditMeetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_meeting)
        val spMeetingType = findViewById<Spinner>(R.id.spMeetingType)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        val spinner = findViewById<Spinner>(R.id.spMeeting)
        val meetingDates = MeetingDatabase(this).getAllMeetingDates()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, meetingDates)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDate = parent?.getItemAtPosition(position).toString()
                val db = MeetingDatabase(this@EditMeetingActivity).writableDatabase
                val cursor = db.query(
                    MeetingDatabase.TABLE_NAME,
                    null,
                    "${MeetingDatabase.COLUMN_DATE} = ?",
                    arrayOf(selectedDate),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    val id = cursor.getInt(cursor.getColumnIndex(MeetingDatabase.COLUMN_ID))
                    val type = cursor.getString(cursor.getColumnIndex(MeetingDatabase.COLUMN_TYPE))
                    val dateString = cursor.getString(cursor.getColumnIndex(MeetingDatabase.COLUMN_DATE))
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)

                    spMeetingType.setSelection(getIndex(spMeetingType, type))
                    datePicker.updateDate(date.year + 1900, date.month, date.date)
                }

                cursor.close()
                db.close()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            // Get the selected meeting date
            val selectedDate = spinner.selectedItem.toString()

            // Get the selected meeting type
            val selectedType = spMeetingType.selectedItem.toString()

            // Get the selected meeting date from the DatePicker
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            // Update the meeting in the database
            val db = MeetingDatabase(this).writableDatabase
            val values = ContentValues().apply {
                put(MeetingDatabase.COLUMN_TYPE, selectedType)
                put(MeetingDatabase.COLUMN_DATE, date)
            }
            db.update(
                MeetingDatabase.TABLE_NAME,
                values,
                "${MeetingDatabase.COLUMN_DATE} = ?",
                arrayOf(selectedDate)
            )

            // Close the database
            db.close()

            // Show a toast to indicate that the meeting was successfully edited
            Toast.makeText(this, "Meeting successfully edited!", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }

        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnDelete.setOnClickListener {
            // Get the selected meeting date
            val selectedDate = spinner.selectedItem.toString()

            // Delete the meeting from the database
            val db = MeetingDatabase(this).writableDatabase
            db.delete(
                MeetingDatabase.TABLE_NAME,
                "${MeetingDatabase.COLUMN_DATE} = ?",
                arrayOf(selectedDate)
            )

            // Close the database
            db.close()

            // Show a toast to indicate that the meeting was successfully deleted
            Toast.makeText(this, "Meeting successfully deleted!", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }
    }
    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

}
