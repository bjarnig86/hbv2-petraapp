package `is`.hi.hbv601g.petraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

    companion object {
        const val TAG: String = "LoginActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mButtonLogin = findViewById(R.id.login_button)
        mButtonLogin.setOnClickListener {
            mEditTextUsername = findViewById(R.id.email_field)
            mEditTextPassword = findViewById(R.id.password_field)

            val email = mEditTextUsername.text.toString()
            val password = mEditTextPassword.text.toString()

            val networkManager = NetworkManager.getInstance(this)
            networkManager.getToken(email, password, object : NetworkCallback<String> {
                override fun onSuccess(result: String) {
                    mToken = result
                    Log.d(TAG, "Token is: $mToken")
                }

                override fun onFailure(errorString: String) {
                    Log.e(TAG, "Failed to get TOKEN!! \n $errorString")
                }
            })
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