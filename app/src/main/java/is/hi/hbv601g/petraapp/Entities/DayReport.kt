package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDateTime

data class DayReport(
    val sleepFrom: LocalDateTime,
    val sleepTo: LocalDateTime,
    val appetite: Int,
    val comment: String,
    val dcwId: Long,
    val childId: Int
)
