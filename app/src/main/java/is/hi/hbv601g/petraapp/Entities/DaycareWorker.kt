package `is`.hi.hbv601g.petraapp.Entities

data class DaycareWorker(
    val id: Int,
    val fullName: String,
    val email: String,
    val address: String,
    val location: String,
    val locationCode: String,
    val freeSpots: Int,
    val experienceInYears: Int,
    val mobile: String
)  {
    fun ifLocationIncludes(s: String): Boolean {
        val combinedValue = "$locationCode $location"
        return s.lowercase() in combinedValue.lowercase()
    }

    fun ifDCWIncludes(s: String): Boolean {
        val combinedValue = "$fullName $email $address $locationCode $location"
        return s.lowercase() in combinedValue.lowercase()
    }

}
