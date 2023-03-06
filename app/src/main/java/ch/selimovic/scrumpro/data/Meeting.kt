package ch.selimovic.scrumpro.data

import android.icu.text.SimpleDateFormat
import java.util.*

data class Meeting(val id: Int, val type: String, val date: Date) {
    val dateFormatted: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
}
