package `is`.hi.hbv601g.petraapp

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DayReportActivity : AppCompatActivity() {

    private lateinit var mChildName: TextView

    companion object {
        const val TAG: String = "DayReportActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dayreport)
        val bundle = intent.extras
        mChildName = findViewById<EditText>(R.id.child_name)

        val childName = bundle?.getString("child")

        if (!childName.isNullOrEmpty()) {
            mChildName.text = childName
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