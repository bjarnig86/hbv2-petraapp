package `is`.hi.hbv601g.petraapp.DTOs

import java.time.LocalDate

data class ParentDTO(
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobile: String,
    val password: String
)
