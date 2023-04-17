package `is`.hi.hbv601g.petraapp.Entities

enum class Appetite {
    BAD,
    OKAY,
    GOOD,
    VERY_GOOD;

    fun getAppetite(str: String): Appetite {
        return when (str) {
            "Vond" -> Appetite.BAD
            "Sæmileg" -> Appetite.OKAY
            "Góð" -> Appetite.GOOD
            "Mjög góð" -> Appetite.VERY_GOOD
            else -> throw IllegalArgumentException("Invalid appetite string: $str")
        }
    }
}