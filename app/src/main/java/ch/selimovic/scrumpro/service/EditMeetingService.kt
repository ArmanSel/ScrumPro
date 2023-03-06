package ch.selimovic.scrumpro.service

import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import ch.selimovic.scrumpro.data.MeetingDatabase
import java.text.SimpleDateFormat
import java.util.*

class EditMeetingService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val selectedDate = intent?.getStringExtra("selected_date")
        val selectedType = intent?.getStringExtra("selected_type")
        val year = intent?.getIntExtra("year", 0) ?: 0
        val month = intent?.getIntExtra("month", 0) ?: 0
        val dayOfMonth = intent?.getIntExtra("dayOfMonth", 0) ?: 0
        val db = MeetingDatabase(this).writableDatabase
        val values = ContentValues().apply {
            put(MeetingDatabase.COLUMN_TYPE, selectedType)
            put(MeetingDatabase.COLUMN_DATE, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(year - 1900, month, dayOfMonth)))
        }
        db.update(
            MeetingDatabase.TABLE_NAME,
            values,
            "${MeetingDatabase.COLUMN_DATE} = ?",
            arrayOf(selectedDate)
        )
        db.close()
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(1000)
        }
        Log.d("Vibrator", "Vibration started")
        stopSelf()
        return START_NOT_STICKY
    }

}
