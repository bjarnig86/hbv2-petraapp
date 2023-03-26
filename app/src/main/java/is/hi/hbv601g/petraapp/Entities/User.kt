package `is`.hi.hbv601g.petraapp.Entities

import com.auth0.android.result.UserProfile
import com.google.gson.reflect.TypeToken
import java.util.Objects

data class User(
    val user: UserProfile? = null
    ) {
    companion object {
        private var instance: UserProfile? = null
        var firstName: String? = null
        var email: String? = null
        var role: String? = null
        fun getInstance(): UserProfile? {
            return instance
        }

        fun setUser(user: UserProfile?) {
            instance = user
        }
    }

}