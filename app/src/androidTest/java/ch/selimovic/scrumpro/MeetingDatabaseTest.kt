package ch.selimovic.scrumpro.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MeetingDatabaseIntegrationTest {
    private lateinit var dbHelper: MeetingDatabase
    private lateinit var db: SQLiteDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = MeetingDatabase(context)
        db = dbHelper.writableDatabase
    }

    @After
    fun cleanup() {
        db.execSQL("DELETE FROM ${MeetingDatabase.TABLE_NAME}")
        db.close()
    }

    @Test
    fun testInsertMeeting() {
        // Create a new meeting and insert it into the database
        val values = ContentValues().apply {
            put(MeetingDatabase.COLUMN_TYPE, "Retro")
            put(MeetingDatabase.COLUMN_DATE, Date().time)
        }
        val newRowId = db.insert(MeetingDatabase.TABLE_NAME, null, values)

        // Check that the insert was successful
        assertTrue(newRowId > -1)

        // Query the database to make sure the meeting was inserted
        val cursor = db.query(
            MeetingDatabase.TABLE_NAME,
            null,
            "${MeetingDatabase.COLUMN_ID} = ?",
            arrayOf(newRowId.toString()),
            null,
            null,
            null
        )
        assertTrue(cursor.moveToFirst())
        assertEquals("Retro", cursor.getString(cursor.getColumnIndex(MeetingDatabase.COLUMN_TYPE)))

        cursor.close()
    }
}
