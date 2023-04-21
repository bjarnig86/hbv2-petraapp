package `is`.hi.hbv601g.petraapp.DTOs

import java.time.LocalDate

data class ParentChildDTO(
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val parentId: String
)
