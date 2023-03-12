package `is`.hi.hbv601g.petraapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mSearchQueryView: EditText
    private lateinit var mSearchQueryBtn: Button
    private lateinit var mButtonLogin: Button
    private lateinit var mButtonRegister: Button
    private lateinit var mButtonDCW: Button
    private lateinit var mButtonParent: Button
    lateinit var DCWList: ArrayList<DaycareWorker>

    companion object {
        const val TAG: String = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            val intent = Intent(this, LoginActivity::class.java)

            // Pass data to SecondActivity (optional)
            // intent.putExtra("message", "Hello from MainActivity!")
            startActivity(intent)
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

        mButtonParent = findViewById(R.id.parent_button)
        mButtonParent.setOnClickListener {
            val intent = Intent(this, ParentActivity::class.java)

            // Pass data to SecondActivity (optional)
            // intent.putExtra("message", "Hello from MainActivity!")
            startActivity(intent)
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