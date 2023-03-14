package `is`.hi.hbv601g.petraapp.Entities

import com.auth0.android.result.UserProfile

data class User(
    val user: UserProfile? = null
    ) {
    companion object {
        private var instance: UserProfile? = null

        fun getInstance(): UserProfile? {
            return instance
        }

        fun setUser(user: UserProfile?) {
            instance = user
        }
    }
}