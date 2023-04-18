package `is`.hi.hbv601g.petraapp.Entities

import java.time.LocalDate

data class Child(
    val id: Int,
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val sicknessDay: LocalDate? = null,
    val daycare_worker_id: Int? = null,
    val parent_id: Int? = null
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}
