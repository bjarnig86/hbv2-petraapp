package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDate

data class ChildDTO(
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val sicknessDay: LocalDate? = null,
    val dayReports: MutableList<DayReport>? = null
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}