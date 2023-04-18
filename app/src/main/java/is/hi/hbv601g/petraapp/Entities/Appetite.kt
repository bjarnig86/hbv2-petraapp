package `is`.hi.hbv601g.petraapp.Entities

enum class Appetite {
    BAD,
    OKAY,
    GOOD,
    VERY_GOOD;

    fun getAppetite(appetite: Appetite): String {
        return when (appetite) {
            Appetite.BAD -> "Ekki vel"
            Appetite.OKAY -> "Ágætlega"
            Appetite.GOOD -> "Vel"
            Appetite.VERY_GOOD -> "Mjög vel"
        }
    }
}