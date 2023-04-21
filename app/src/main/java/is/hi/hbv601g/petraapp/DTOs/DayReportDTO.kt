package `is`.hi.hbv601g.petraapp.DTOs

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class DayReportDTO (
    val id: Int,
    val date: Date,
    val sleepFrom: LocalDateTime,
    val sleepTo: LocalDateTime,
    val appetite: String,
    val comment: String,
) {}