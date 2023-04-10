package `is`.hi.hbv601g.petraapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.auth0.android.result.UserProfile
import `is`.hi.hbv601g.petraapp.Entities.FullDCW
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.MainActivity
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import `is`.hi.hbv601g.petraapp.utils.SharedPreferencesUtil

private lateinit var accessToken: String

class BottomNav {
    companion object {
        private const val LOGGED_IN_TAG = "LOGGED_IN"
        private const val NOT_LOGGED_IN_TAG = "NOT_LOGGED_IN"
    }

    private fun getLoggedInFragment(): Fragment {
        return BottomNavLoggedIn()
    }

    private fun getNotLoggedInFragment(): Fragment {
        return BottomNavNotLoggedIn()
    }

    private fun displayFragment(fragmentManager: FragmentManager, containerViewId: Int) {
        val transaction = fragmentManager.beginTransaction()
        val fragment: Fragment = if (User.getInstance() == null) {
            getNotLoggedInFragment()
        } else {
            getLoggedInFragment()
        }

        transaction.add(containerViewId, fragment)
        transaction.commit()
    }

    fun handleFragments(context: Context, fragmentManager: FragmentManager, containerViewId: Int) {
        val prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        accessToken = prefs.getString("ACCESS_TOKEN", "").toString()

        val networkManager = NetworkManager.getInstance(context)

        if (accessToken.isNotBlank()) {
            val role = prefs.getString("USER_ROLE", "").toString()
            val user: Any
            if (role == "DCW") {
                user = SharedPreferencesUtil.fetchUserFromSharedPreferences(context, role) as FullDCW
                User.firstName = user.firstName
            } else if (role == "Parent") {
                user = SharedPreferencesUtil.fetchUserFromSharedPreferences(context, role) as Parent
                User.firstName = user.firstName
            }

            networkManager.getUserInfo(accessToken, object: NetworkCallback<UserProfile> {
                override fun onSuccess(result: UserProfile) {
                    User.setUser(result)
                    User.role = role
                    Log.d(MainActivity.TAG, "This is a log message from ${Thread.currentThread().stackTrace[2].methodName}() at line ${Thread.currentThread().stackTrace[2].lineNumber}")
                    Log.d(MainActivity.TAG, "onSuccess: SUCCESS ${result.email}")
                    displayFragment(fragmentManager, containerViewId)
                }

                override fun onFailure(errorString: String) {
                    User.setUser(null)
                    Log.e(MainActivity.TAG, "This is a log message from ${Thread.currentThread().stackTrace[2].methodName}() at line ${Thread.currentThread().stackTrace[2].lineNumber}")
                    Log.e(MainActivity.TAG, "onFailure: User set to NULL", )
                    Log.e(MainActivity.TAG, "onFailure: FAILED $errorString", )
                    displayFragment(fragmentManager, containerViewId)
                }
            })
        } else {
            displayFragment(fragmentManager, containerViewId)
        }
    }
}