package `is`.hi.hbv601g.petraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import `is`.hi.hbv601g.petraapp.databinding.ActivityLoginBinding
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var mButtonLogin: Button
    private lateinit var mButtonRegister: Button
    private lateinit var mEditTextUsername: EditText
    private lateinit var mEditTextPassword: EditText

    private lateinit var mToken: String
    private lateinit var account: Auth0


    companion object {
        const val TAG: String = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        account = Auth0(
            clientId = this.getString(R.string.auth0_client_id),
            domain = this.getString(R.string.auth0_domain)
        )

        mButtonLogin = findViewById(R.id.login_button)
        mButtonLogin.setOnClickListener {
            loginWithBrowser()

            mButtonLogin.visibility = View.GONE
        }

    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
            .withScheme("demo")
            .withScope("openid profile email")
            .withAudience("https://dev-xzuj3qsd.eu.auth0.com/api/v2/")
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    Log.d(TAG, "onFailure: $exception")
                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val accessToken = credentials.accessToken
                    mToken = accessToken
                    val authClient = AuthenticationAPIClient(account)
                    authClient.userInfo(mToken).start(object : Callback<UserProfile, AuthenticationException> {
                        override fun onSuccess(payload: UserProfile) {
                            // Store the user's profile in your app's memory or persistent storage
                            val user = payload
                            // Handle the user object
                            Log.d(TAG, "onSuccess: ${user.email}")
                        }

                        override fun onFailure(error: AuthenticationException) {
                            // Handle the error
                        }
                    })
                    Log.d(TAG, "Access Token: $mToken")
                }
            })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }
}