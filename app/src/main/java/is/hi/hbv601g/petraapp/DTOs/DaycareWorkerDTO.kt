package `is`.hi.hbv601g.petraapp.DTOs

data class DaycareWorkerDTO(
    val firstName: String,
    val lastName: String,
    val email: String,
    val ssn: String,
    val address: String,
    val location: String,
    val locationCode: String,
    val experienceInYears: Int,
    val mobile: String,
    val password: String
)  {
    override fun toString(): String {
        return super.toString()
    }

}
