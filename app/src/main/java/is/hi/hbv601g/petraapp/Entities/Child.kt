package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDate
import java.util.*

data class Child(
    val id: Int,
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val sicknessDay: Date? = null,
    val daycare_worker_id: Int? = null,
    val dcwName: String? = null,
    val dcwMobile: String? = null,
    val dcwEmail: String? = null,
    val parent_id: Int? = null
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}
