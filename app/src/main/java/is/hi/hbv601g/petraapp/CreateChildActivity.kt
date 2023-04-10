package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class CreateChildActivity : AppCompatActivity() {

    private lateinit var mCreateChildButton: Button
    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var mSSN: EditText
    val networkManager = NetworkManager.getInstance(this)
    companion object {
        const val TAG: String = "DayReportActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_child)
        val preferences = getSharedPreferences("MY_APP_PREFS",
            Context.MODE_PRIVATE);
        mCreateChildButton = findViewById(R.id.createChildButton)
        val gson = Gson()
        val json: String? = preferences.getString("USER_KEY", "children")
        val parent: Parent = gson.fromJson(json, Parent::class.java)

        mCreateChildButton.setOnClickListener{
            mFirstName = findViewById(R.id.childsFirstName)
            mLastName = findViewById(R.id.childsLastName)
            mSSN = findViewById(R.id.ssn)
            val child = Child(mSSN.text.toString(), mFirstName.text.toString(), mLastName.text.toString())
            networkManager.createChild(child, parent, object: NetworkCallback<Child>{
                override fun onSuccess(result: Child) {
                    finishActivity(0)
                }

                override fun onFailure(errorString: String) {
                    Log.e(TAG, "onFailure: createChild: $errorString", )
                }
            })
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