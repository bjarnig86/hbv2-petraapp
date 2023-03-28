package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.*
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.Entities.FullDCW
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.databinding.ActivityMainBinding
import `is`.hi.hbv601g.petraapp.fragments.BottomNavLoggedIn
import `is`.hi.hbv601g.petraapp.fragments.BottomNavNotLoggedIn
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mSearchQueryView: AutoCompleteTextView
    private lateinit var mSearchQueryBtn: Button
    private lateinit var mButtonLogin: Button
//    private lateinit var mButtonRegister: Button
//    private lateinit var mButtonDCW: Button
//    private lateinit var mButtonParent: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mDCWRecyclerView: RecyclerView

    private var mDCWList = mutableListOf<DaycareWorker>()
    private var mLocationList = ArrayList<String>()


    private lateinit var account: Auth0
    private lateinit var accessToken: String
    private lateinit var auth0Client: AuthenticationAPIClient
    private lateinit var mCustomActionBarGreeting: TextView
    lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkManager = NetworkManager.getInstance(this)



        // initialize the auth0 account
        account = Auth0(
            clientId = this.getString(R.string.auth0_client_id),
            domain = this.getString(R.string.auth0_domain)
        )

        // retrieve user if in sharedPrefrences
        val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        accessToken = prefs.getString("ACCESS_TOKEN", "").toString()

        if (accessToken.isNotBlank()) {
            val role = prefs.getString("USER_ROLE", "").toString()
            val user: Any
            if (role == "DCW") {
                user = fetchUserFromSharedPreferences(role) as FullDCW
                User.firstName = user.firstName
            } else if (role == "Parent") {
                user = fetchUserFromSharedPreferences(role) as Parent
                User.firstName = user.firstName
            }

            networkManager.getUserInfo(accessToken, object: NetworkCallback<UserProfile> {
                override fun onSuccess(result: UserProfile) {
                    User.setUser(result)
                    User.role = role
//                    handleButtonsOnLoginAndLogout()
                    handleGreetingInActionBar()
                    setFragment(BottomNavLoggedIn());
                    Log.d(TAG, "This is a log message from ${Thread.currentThread().stackTrace[2].methodName}() at line ${Thread.currentThread().stackTrace[2].lineNumber}")
                    Log.d(TAG, "onSuccess: SUCCESS ${result.email}")
                }

                override fun onFailure(errorString: String) {
                    User.setUser(null)
                    setFragment(BottomNavNotLoggedIn());
                    Log.e(TAG, "This is a log message from ${Thread.currentThread().stackTrace[2].methodName}() at line ${Thread.currentThread().stackTrace[2].lineNumber}")
                    Log.e(TAG, "onFailure: User set to NULL", )
                    Log.e(TAG, "onFailure: FAILED $errorString", )
                }

            })
        } else {
            setFragment(BottomNavNotLoggedIn());
        }


        // get the progress bar
        mProgressBar = findViewById(R.id.progress_bar)

        // Search input logic
        mSearchQueryView = findViewById(R.id.searchQuery)
        mSearchQueryBtn = findViewById(R.id.searchButton)

        // location shiiitt
        networkManager.getLocations(object : NetworkCallback<ArrayList<String>> {
            override fun onSuccess(result: ArrayList<String>) {
                mLocationList = result
                Log.d(TAG, "onSuccess: ${mLocationList[0]}")

                val locationAdapter: ArrayAdapter<String> = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, mLocationList)
                mSearchQueryView.setAdapter(locationAdapter)
                mSearchQueryView.addTextChangedListener(object: TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        locationAdapter.filter.filter(p0)
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        if (mSearchQueryView.text.isBlank()) {
                            val adapter = DaycareWorkerCardAdapter(mDCWList)
                            mDCWRecyclerView.adapter = adapter

                            this@MainActivity.currentFocus?.let { view ->
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                        }
                    }

                })
            }

            override fun onFailure(errorString: String) {
                Log.e(TAG, "onFailure: Getting locations failed!", )
            }
        })


        mSearchQueryBtn.setOnClickListener {
            if (mSearchQueryView.text.isNotEmpty()) {
                Toast.makeText(this, mSearchQueryView.text, Toast.LENGTH_SHORT).show()
                val filteredList: MutableList<DaycareWorker> = ArrayList()
                mDCWList.map {
                    dcw ->
                    if (dcw.ifLocationIncludes(mSearchQueryView.text.toString())) {
                        filteredList.add(dcw)
                    }
                }

                val filteredAdapter = DaycareWorkerCardAdapter(filteredList)
                mDCWRecyclerView.adapter = filteredAdapter

                Toast.makeText(this@MainActivity, "${filteredList.size} dagforeldri fundust!", Toast.LENGTH_SHORT).show()


                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } else {
                val filteredAdapter = DaycareWorkerCardAdapter(mDCWList)
                mDCWRecyclerView.adapter = filteredAdapter

                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        // Daycare worker (DCW) recycler logic
        mDCWRecyclerView = findViewById<View>(R.id.rvDaycareWorkers) as RecyclerView
        networkManager.getDCWs( object: NetworkCallback<List<DaycareWorker>> {
            override fun onSuccess(result: List<DaycareWorker>) {
                mDCWList = result.toMutableList()
                Log.d(TAG, "Successfully fetched DCWs ${mDCWList.size}")
                // List of cards logic
                val adapter = DaycareWorkerCardAdapter(mDCWList);
                mDCWRecyclerView.adapter = adapter;
                mDCWRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity);
                mDCWRecyclerView.visibility = View.VISIBLE
                mProgressBar.visibility = View.GONE
            }

            override fun onFailure(errorString: String) {
                Log.d(TAG, "Failed to get DCWs: $errorString")
            }
        })



        // Bottom screen button logic
    //        mButtonLogin = findViewById(R.id.login_button)
    //        mButtonLogin.setOnClickListener {
    //            if (User.getInstance() == null) {
    //                loginWithBrowser()
    //            } else {
    //                logout()
    //            }
    //        }

//
//        mButtonRegister = findViewById(R.id.register_button)
//        mButtonRegister.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//
//        mButtonDCW = findViewById(R.id.dcw_button)
//        mButtonDCW.setOnClickListener {
//            val intent = Intent(this, DcwActivity::class.java)
//
//            // Pass data to SecondActivity (optional)
//            // intent.putExtra("message", "Hello from MainActivity!")
//            startActivity(intent)
//        }
//
//        mButtonParent = findViewById(R.id.parent_button)
//        mButtonParent.setOnClickListener {
//            val intent = Intent(this, ParentActivity::class.java)
//
//            // Pass data to SecondActivity (optional)
//            // intent.putExtra("message", "Hello from MainActivity!")
//            startActivity(intent)
//        }

    }

    fun loginWithBrowser() {
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
                            User.setUser(user)
                            getUserInfoAndStoreInMemory(User.getInstance(), accessToken)
//                            handleButtonsOnLoginAndLogout()
                            Log.d(TAG, "onLogin: ${user.getId()}")
                        }

                        override fun onFailure(error: AuthenticationException) {
                            Log.e(TAG, "onFailure: $error")
                        }
                    })
                    Log.d(TAG, "Access Token: $accessToken")
                }
            })
    }

    private fun getUserInfoAndStoreInMemory(user: UserProfile?, accessToken: String, role: String = "") {
        Log.d(TAG, "getUserInfo: $user")
        val userRole = role.ifBlank { (user?.getExtraInfo()?.get("https://petraapp.com/userRoles") as ArrayList<*>)[0] as String? }
        val userEmail = user?.email
        val auth0id = user?.getId() as String
        val networkManager = NetworkManager.getInstance(this)
        
        if (userRole == "Parent") {
            networkManager.getParent(auth0id, object: NetworkCallback<Parent>{
                override fun onSuccess(result: Parent) {
                    Log.d(TAG +"userinfo", "onSuccess: $result")
                    User.firstName = result.firstName
                    User.role = userRole
                    handleGreetingInActionBar()
                    setFragment(BottomNavLoggedIn());

                    // save to sharedpreferences
                    saveUserToSharedPreferences(result)
                    saveStringToSharedPreferences("ACCESS_TOKEN", accessToken)
                    saveStringToSharedPreferences("USER_ID", auth0id)
                    saveStringToSharedPreferences("USER_ROLE", userRole)
                }

                override fun onFailure(errorString: String) {
                    setFragment(BottomNavNotLoggedIn());
                    Log.e(TAG +"userinfo", "onFailure: $errorString", )
                }
            })
        } else if (userRole == "DCW") {
            networkManager.getFullDCW(userEmail, object: NetworkCallback<FullDCW>{
                override fun onSuccess(result: FullDCW) {
                    Log.d("$TAG userinfo", "onSuccess: $result")
                    User.firstName = result.firstName
                    User.role = userRole
                    handleGreetingInActionBar()
                    setFragment(BottomNavLoggedIn());

                    // save to sharedpreferences
                    saveUserToSharedPreferences(result)
                    saveStringToSharedPreferences("ACCESS_TOKEN", accessToken)
                    saveStringToSharedPreferences("USER_ID", auth0id)
                    saveStringToSharedPreferences("USER_ROLE", userRole)
                }

                override fun onFailure(errorString: String) {
                    setFragment(BottomNavNotLoggedIn());
                    Log.e(TAG, "getUserInfo: onFailure: $errorString", )
                }
            })
        } else {
//            TODO("if there is no type on user???")
            Log.e(TAG, "getUserInfo: user has no roles on Auth0", )
        }
    }

    private fun saveUserToSharedPreferences(value: Any) {
        val gson = Gson()
        val json = gson.toJson(value)
        val sharedPreferences = getSharedPreferences(
            "MY_APP_PREFS",
            Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("USER_KEY", json).apply()
    }

    private fun clearSharedPreferences() {
        val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        Log.d(TAG, "User was cleared from sharedPrefs!")
    }



    private fun fetchUserFromSharedPreferences(role: String): Any {
        val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val userString = prefs.getString("USER_KEY", "")
        val gson = Gson()
        return when (role) {
            "DCW" -> gson.fromJson(userString, FullDCW::class.java)
            "Parent" -> gson.fromJson(userString, Parent::class.java)
            else -> throw Error("No role found to determine class!")
        }
    }

    private fun saveStringToSharedPreferences(key: String, value: String) {
        val sharedPreferences = getSharedPreferences(
            "MY_APP_PREFS",
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString(key, value).apply()
    }


    fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    // The user has been logged out!
                    User.setUser(null)
                    handleGreetingInActionBar()
//                    handleButtonsOnLoginAndLogout()
                    clearSharedPreferences()
                }

                override fun onFailure(error: AuthenticationException) {
                    // TODO: not yet implemented (maybe just log this...)
                    Log.e(TAG, "onFailure: Failed to finish the logout", error)
                }
            })
    }

//    fun handleButtonsOnLoginAndLogout() {
//        if (User.getInstance() != null) {
//            mButtonLogin.text = getString(R.string.logout_button)
//            mButtonRegister.visibility = View.GONE
//        } else {
//            mButtonLogin.text = getString(R.string.login_button)
//            mButtonRegister.visibility = View.VISIBLE
//        }
//    }


    fun handleGreetingInActionBar() {
        if (User.getInstance() != null) {
            mCustomActionBarGreeting = findViewById(R.id.custom_action_bar_greeting_text)
            mCustomActionBarGreeting.text = getString(R.string.greeting_message, User.firstName, User.role)
        } else {
            mCustomActionBarGreeting = findViewById(R.id.custom_action_bar_greeting_text)
            mCustomActionBarGreeting.text = ""
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav, fragment);
        fragmentTransaction.commit();
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