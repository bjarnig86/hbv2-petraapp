package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDateTime
import java.util.*

data class DayReport(
    val sleepFrom: Date,
    val sleepTo: Date,
    val appetite: String,
    val comment: String,
    val dcwId: Long,
    val childId: Long
)
