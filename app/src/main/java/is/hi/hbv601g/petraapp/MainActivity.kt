package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.Entities.User

class MainActivity : AppCompatActivity() {

    private lateinit var mSearchQueryView: EditText
    private lateinit var mSearchQueryBtn: Button
    private lateinit var mButtonLogin: Button
    private lateinit var mButtonRegister: Button
    private lateinit var mButtonDCW: Button
    lateinit var DCWList: ArrayList<DaycareWorker>

    private lateinit var account: Auth0
    private lateinit var mCustomActionBarGreeting: TextView

    companion object {
        const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize the auth0 account
        account = Auth0(
            clientId = this.getString(R.string.auth0_client_id),
            domain = this.getString(R.string.auth0_domain)
        )

        // Search input logic
        mSearchQueryView = findViewById<EditText>(R.id.searchQuery)
        mSearchQueryBtn = findViewById<Button>(R.id.searchButton)

        mSearchQueryView.setOnKeyListener {_, keyCode, event ->
            if (mSearchQueryView.text.isNotEmpty()) {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(this, mSearchQueryView.text, Toast.LENGTH_SHORT).show()

                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        mSearchQueryBtn.setOnClickListener {
            if (mSearchQueryView.text.isNotEmpty()) {
                Toast.makeText(this, mSearchQueryView.text, Toast.LENGTH_SHORT).show()

                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        // List of cards logic
        val dcwRecyclerView = findViewById<View>(R.id.rvDaycareWorkers) as RecyclerView
        DCWList = arrayListOf(
            DaycareWorker(
                id = 1,
                fullName = "Maiya Cartwright",
                email = "Glennie_Beatty93@gmail.com",
                address = "6604 Farrell Island",
                location = "Grenivík",
                locationCode = "616",
                freeSpots = 5,
                experienceInYears = 304,
                mobile = "5812345"
            ),
            DaycareWorker(
                id = 2,
                fullName = "Rico Beier",
                email = "Ryley37@gmail.com",
                address = "016 Daugherty Loaf",
                location = "Hvolsvelli",
                locationCode = "860",
                freeSpots = 5,
                experienceInYears = 4,
                mobile = "5812345"
            ),
            DaycareWorker(
                id = 3,
                fullName = "Naomie Swaniawski",
                email = "Claud85@hotmail.com",
                address = "28162 Haylie Landing",
                location = "Súðavík",
                locationCode = "420",
                freeSpots = 5,
                experienceInYears = 5,
                mobile = "5812345"
            ),
            DaycareWorker(
                id = 4,
                fullName = "Zula Corkery",
                email = "Gene64@yahoo.com",
                address = "558 Keebler Square",
                location = "Bíldudal",
                locationCode = "466",
                freeSpots = 5,
                experienceInYears = 6,
                mobile = "5812345"
            ),
            DaycareWorker(
                id = 5,
                fullName = "Aida Cassin",
                email = "Icie.Miller21@yahoo.com",
                address = "716 Zemlak Trace",
                location = "Akureyri",
                locationCode = "603",
                freeSpots = 5,
                experienceInYears = 4,
                mobile = "5812345"
            )
        );
        val adapter = DaycareWorkerCardAdapter(DCWList);
        dcwRecyclerView.adapter = adapter;
        dcwRecyclerView.layoutManager = LinearLayoutManager(this);

        // Bottom screen button logic
        mButtonLogin = findViewById(R.id.login_button)
        mButtonLogin.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)

            // Pass data to SecondActivity (optional)
            // intent.putExtra("message", "Hello from MainActivity!")
//            startActivity(intent)
            if (User.getInstance() == null) {
                loginWithBrowser()
            } else {
                logout()
            }
        }

        mButtonRegister = findViewById(R.id.register_button)
        mButtonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        mButtonDCW = findViewById(R.id.dcw_button)
        mButtonDCW.setOnClickListener {
            val intent = Intent(this, DcwActivity::class.java)

            // Pass data to SecondActivity (optional)
            // intent.putExtra("message", "Hello from MainActivity!")
            startActivity(intent)
        }

    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
            .withScheme("demo")
            .withScope("openid profile email")
            .withAudience("https://dev-xzuj3qsd.eu.auth0.com/api/v2/")
            // Launch the authentication passing the callback where the results will be received
            .start(this@MainActivity, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(error: AuthenticationException) {
                    Log.d(TAG, "onFailure: $error")
                }

                // Called when authentication completed successfully
                override fun onSuccess(result: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val accessToken = result.accessToken
                    val authClient = AuthenticationAPIClient(account)
                    authClient.userInfo(accessToken).start(object :
                        Callback<UserProfile, AuthenticationException> {
                        override fun onSuccess(result: UserProfile) {
                            // Store the user's profile in your app's memory or persistent storage
                            val user = result
                            // Handle the user object
                            User.setUser(user)
                            handleButtonsOnLoginAndLogout()
                            handleGrettingInActionBar(User.getInstance())
                            Log.d(TAG, "onSuccess: ${user.givenName}")
                            Log.d(TAG, "onSuccess: ${user.familyName}")
                            Log.d(TAG, "onSuccess: ${user.name}")
                            Log.d(TAG, "onSuccess: ${user.isEmailVerified}")
                            Log.d(TAG, "onSuccess: ${user.nickname}")
                            Log.d(TAG, "onSuccess: ${user.getExtraInfo()["roles"]}")
                        }

                        override fun onFailure(error: AuthenticationException) {
                            // Handle the error
                        }
                    })
                    Log.d(TAG, "Access Token: $accessToken")
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    // The user has been logged out!
                    User.setUser(null)
                    handleButtonsOnLoginAndLogout()
                    handleGrettingInActionBar(User.getInstance())
                }

                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                }
            })
    }

    fun handleButtonsOnLoginAndLogout() {
        if (User.getInstance() != null) {
            mButtonLogin.text = getString(R.string.logout_button)
            mButtonRegister.visibility = View.GONE
        } else {
            mButtonLogin.text = getString(R.string.login_button)
            mButtonRegister.visibility = View.VISIBLE
        }
    }


    fun handleGrettingInActionBar(userProfile: UserProfile?) {
        if (User.getInstance() != null) {
            mCustomActionBarGreeting = findViewById(R.id.custom_action_bar_greeting_text)
            mCustomActionBarGreeting.text = "Hæ ${userProfile?.nickname}!"
        } else {
            mCustomActionBarGreeting = findViewById(R.id.custom_action_bar_greeting_text)
            mCustomActionBarGreeting.text = ""
        }
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