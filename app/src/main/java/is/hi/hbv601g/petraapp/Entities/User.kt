package `is`.hi.hbv601g.petraapp.Entities

import com.auth0.android.Auth0
import com.auth0.android.result.UserProfile
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.petraapp.R
import java.util.Objects

data class User(
    val user: UserProfile? = null
    ) {
    companion object {
        private var instance: UserProfile? = null
        var firstName: String? = null
        var email: String? = null
        var id: Long? = null
        var role: String? = null
        var account = Auth0(
            clientId = "ox325QZVYQitbySZYq0CZOW5vJLs9r4Q",
            domain = "dev-xzuj3qsd.eu.auth0.com"
        )
        fun getInstance(): UserProfile? {
            return instance
        }

        fun setUser(user: UserProfile?) {
            instance = user
        }
    }

}