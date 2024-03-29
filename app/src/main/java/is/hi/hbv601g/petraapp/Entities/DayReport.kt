package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDateTime
import java.util.Date

data class DayReport(
    val date: Date? = null,
    val sleepFrom: LocalDateTime,
    val sleepTo: LocalDateTime,
    val appetite: String,
    val comment: String,
    val dcwId: Long,
    val childId: Int
)
