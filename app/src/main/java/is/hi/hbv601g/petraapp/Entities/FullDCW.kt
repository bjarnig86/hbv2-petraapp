package `is`.hi.hbv601g.petraapp.Entities


data class FullDCW(
    var id: Int?,
    var ssn: String?,
    var fullName: String?,
    var firstName: String?,
    var lastName: String?,
    var mobile: String?,
    var email: String?,
    var auth0Id: String?,
    var experienceInYears: Int?,
    var address: String?,
    var location: String?,
    var locationCode: String?,
    var childrenCount: Int?,
    var children: MutableList<Child>?,
    var waitingList: MutableList<ApplicationDTO>?,
    var dayReports: MutableList<DayReport>?,
    var type: String?,
    var password: String?,
    var maxchildren: String?,
    var freeSpots: String?
) {
}

