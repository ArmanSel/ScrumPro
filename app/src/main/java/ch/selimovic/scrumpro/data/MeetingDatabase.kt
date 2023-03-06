package ch.selimovic.scrumpro.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class MeetingDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MeetingDatabase"
        const val TABLE_NAME = "meetings"
        const val COLUMN_ID = "_id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_MEETING_TABLE =
            ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TYPE TEXT, $COLUMN_DATE DATE)")
        db?.execSQL(CREATE_MEETING_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllMeetings(): List<Meeting> {
        val meetings = mutableListOf<Meeting>()

        // Select all rows from the meetings table
        val selectQuery = "SELECT * FROM ${MeetingDatabase.TABLE_NAME}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // Loop through all rows and create Meeting objects
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(MeetingDatabase.COLUMN_ID))
                val type = cursor.getString(cursor.getColumnIndex(MeetingDatabase.COLUMN_TYPE))
                val dateString = cursor.getString(cursor.getColumnIndex(MeetingDatabase.COLUMN_DATE))
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)

                meetings.add(Meeting(id, type, date))
            } while (cursor.moveToNext())
        }

        cursor.close()

        return meetings
    }
}