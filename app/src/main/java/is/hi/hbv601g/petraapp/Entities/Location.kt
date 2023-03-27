package `is`.hi.hbv601g.petraapp.Entities

data class Location(
    val locationName: String,
    val locationCode: String
) {
    override fun toString(): String {
        return "$locationCode $locationName"
    }
}
