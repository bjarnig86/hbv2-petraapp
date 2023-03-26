package `is`.hi.hbv601g.petraapp.Entities

data class Parent(
    val id: Int,
    val ssn: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobile: String,
    val auth0Id: String,
    val children: MutableList<Child>,
    val type: String
) {

}

