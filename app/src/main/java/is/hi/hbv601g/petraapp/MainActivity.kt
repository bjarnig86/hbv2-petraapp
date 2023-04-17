package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.result.UserProfile
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.Entities.FullDCW
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.databinding.ActivityMainBinding
import `is`.hi.hbv601g.petraapp.fragments.BottomNav
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import `is`.hi.hbv601g.petraapp.utils.SharedPreferencesUtil

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mSearchQueryView: AutoCompleteTextView
    private lateinit var mSearchQueryBtn: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mDCWRecyclerView: RecyclerView

    private var mDCWList = mutableListOf<DaycareWorker>()
    private var mLocationList = ArrayList<String>()


    private lateinit var accessToken: String
    private lateinit var userRole: String
    lateinit var binding: ActivityMainBinding
    private val bottomNav = BottomNav()

    companion object {
        const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav.handleFragments(this, supportFragmentManager, R.id.bottom_nav)

        val networkManager = NetworkManager.getInstance(this)

        // retrieve user if in sharedPrefrences
        val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        accessToken = prefs.getString("ACCESS_TOKEN", "").toString()
        userRole = prefs.getString("USER_ROLE", null).toString()

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
                            val adapter = DaycareWorkerCardAdapter(mDCWList, this@MainActivity, supportFragmentManager)
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

                val filteredAdapter = DaycareWorkerCardAdapter(filteredList, this, supportFragmentManager)
                mDCWRecyclerView.adapter = filteredAdapter

                Toast.makeText(this@MainActivity, "${filteredList.size} dagforeldri fundust!", Toast.LENGTH_SHORT).show()


                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } else {
                val filteredAdapter = DaycareWorkerCardAdapter(mDCWList, this, supportFragmentManager)
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
                val adapter = DaycareWorkerCardAdapter(mDCWList, this@MainActivity, supportFragmentManager);
                mDCWRecyclerView.adapter = adapter;
                mDCWRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity);
                mDCWRecyclerView.visibility = View.VISIBLE
                mProgressBar.visibility = View.GONE
            }

            override fun onFailure(errorString: String) {
                Log.d(TAG, "Failed to get DCWs: $errorString")
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