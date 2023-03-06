package ch.selimovic.scrumpro.domain

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import ch.selimovic.scrumpro.data.MeetingDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewMeetingPresenter(private val context: Context, private val database: MeetingDatabase) {
    fun createNewMeeting(type: String, date: LocalDate) {
        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

        // Insert a new row into the meetings table
        val values = ContentValues().apply {
            put(MeetingDatabase.COLUMN_TYPE, type)
            put(MeetingDatabase.COLUMN_DATE, formattedDate)
        }
        database.writableDatabase.insert(MeetingDatabase.TABLE_NAME, null, values)

        // Show a toast message to indicate success
        Toast.makeText(context, "Meeting created", Toast.LENGTH_SHORT).show()
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(1000)
        }
        Log.d("Vibrator", "Vibration started")
    }
}
