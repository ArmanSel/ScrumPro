package ch.selimovic.scrumpro.domain

class MeetingPresenter(private val repository: MeetingRepository) {

    fun getFormattedMeetingList(): String {
        val meetings = repository.getAllMeetings()

        return meetings.joinToString(separator = "\n") { meeting ->
            "${meeting.type} - ${meeting.dateFormatted}"
        }
    }
}
