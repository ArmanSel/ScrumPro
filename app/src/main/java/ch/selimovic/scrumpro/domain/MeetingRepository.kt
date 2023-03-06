package ch.selimovic.scrumpro.domain

import ch.selimovic.scrumpro.data.Meeting
import ch.selimovic.scrumpro.data.MeetingDatabase

class MeetingRepository(private val database: MeetingDatabase) {

    fun getAllMeetings(): List<Meeting> {
        return database.getAllMeetings()
    }
}
