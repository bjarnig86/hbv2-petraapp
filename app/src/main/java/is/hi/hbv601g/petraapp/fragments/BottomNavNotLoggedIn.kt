package `is`.hi.hbv601g.petraapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import `is`.hi.hbv601g.petraapp.*
import `is`.hi.hbv601g.petraapp.Entities.FullDCW
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import `is`.hi.hbv601g.petraapp.utils.SharedPreferencesUtil

class BottomNavNotLoggedIn : Fragment() {
    private lateinit var mButtonLogin: Button
    private lateinit var mButtonRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.menu_dock_not_logged_in, container, false)
        mButtonLogin = root.findViewById(R.id.login_button)
        mButtonRegister = root.findViewById(R.id.register_button)
        mButtonLogin.setOnClickListener {
            loginWithBrowser(requireContext())
        }
        mButtonRegister.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        return root
    }

    fun loginWithBrowser(context: Context) {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(User.account)
            .withScheme("demo")
            .withScope("openid profile email")
            .withAudience("https://dev-xzuj3qsd.eu.auth0.com/api/v2/")
            // Launch the authentication passing the callback where the results will be received
            .start(context, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(error: AuthenticationException) {
                    Log.d(MainActivity.TAG, "onFailure: $error")
                }

                // Called when authentication completed successfully
                override fun onSuccess(result: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val accessToken = result.accessToken
                    val authClient = AuthenticationAPIClient(User.account)
                    authClient.userInfo(accessToken).start(object :
                        Callback<UserProfile, AuthenticationException> {
                        override fun onSuccess(result: UserProfile) {
                            // Store the user's profile in your app's memory or persistent storage
                            val user = result
                            User.setUser(user)
                            getUserInfoAndStoreInMemory(context, User.getInstance(), accessToken)
                            Log.d(MainActivity.TAG, "onLogin: ${user.getId()}")
                        }

                        override fun onFailure(error: AuthenticationException) {
                            Log.e(MainActivity.TAG, "onFailure: $error")
                        }
                    })
                    Log.d(MainActivity.TAG, "Access Token: $accessToken")
                }
            })
    }

    private fun getUserInfoAndStoreInMemory(context: Context, user: UserProfile?, accessToken: String, role: String = "") {
        Log.d(MainActivity.TAG, "getUserInfo: $user")
        val userRole = role.ifBlank { (user?.getExtraInfo()?.get("https://petraapp.com/userRoles") as ArrayList<*>)[0] as String? }
        val userEmail = user?.email
        val auth0id = user?.getId() as String
        val networkManager = NetworkManager.getInstance(context)

        if (userRole == "Parent") {
            networkManager.getParent(auth0id, object: NetworkCallback<Parent> {
                override fun onSuccess(result: Parent) {
                    Log.d(MainActivity.TAG +"userinfo", "onSuccess: $result")
                    User.firstName = result.firstName
                    User.role = userRole
                    User.id = result.id.toLong()

                    // save to sharedpreferences
                    SharedPreferencesUtil.saveUserToSharedPreferences(context, result)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context, "ACCESS_TOKEN", accessToken)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context,"USER_ID", auth0id)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context,"PARENT_ID", result.id.toString())
                    SharedPreferencesUtil.saveStringToSharedPreferences(context,"USER_ROLE", userRole)

                    val intent = Intent(requireContext(), ParentActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                override fun onFailure(errorString: String) {
                    Log.e(MainActivity.TAG +"userinfo", "onFailure: $errorString", )
                }
            })
        } else if (userRole == "DCW") {
            networkManager.getFullDCW(userEmail, object: NetworkCallback<FullDCW> {
                override fun onSuccess(result: FullDCW) {
                    Log.d("${MainActivity.TAG} userinfo", "onSuccess: $result")
                    User.firstName = result.firstName
                    User.role = userRole

                    // save to sharedpreferences
                    SharedPreferencesUtil.saveUserToSharedPreferences(context, result)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context, "ACCESS_TOKEN", accessToken)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context, "USER_ID", auth0id)
                    SharedPreferencesUtil.saveStringToSharedPreferences(context, "USER_ROLE", userRole)
                    val intent = Intent(requireContext(), DcwActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                override fun onFailure(errorString: String) {
                    Log.e(MainActivity.TAG, "getUserInfo: onFailure: $errorString", )
                }
            })
        } else {
//            TODO("if there is no type on user???")
            Log.e(MainActivity.TAG, "getUserInfo: user has no roles on Auth0", )
        }
    }


}