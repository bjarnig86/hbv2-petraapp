package `is`.hi.hbv601g.petraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var mButtonLogin: Button
    private lateinit var mButtonRegister: Button
    private lateinit var mEditTextUsername: EditText
    private lateinit var mEditTextPassword: EditText

    companion object {
        const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButtonLogin = findViewById(R.id.login_button)
        mButtonLogin.setOnClickListener {
            mEditTextUsername = findViewById(R.id.email_field)
            mEditTextPassword = findViewById(R.id.register_button)

            val email = mEditTextUsername.text
            val password = mEditTextUsername.text

            val endpoint = "https://${R.string.auth0_domain}/oauth/ro"

            val headers = JSONObject()
            headers.put("Content-Type", "application/json")

            val body = JSONObject()
            body.put("client_id", R.string.auth0_client_id)
            body.put("username", email)
            body.put("password", password)
            body.put("connection", "CONNECTION")
            body.put("scope", "openid")


        }

        mButtonRegister = findViewById(R.id.register_button)
        mButtonRegister.setOnClickListener {
            Toast.makeText(this, "HEY DABBA!", Toast.LENGTH_SHORT).show()
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