package `is`.hi.hbv601g.petraapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText


class RegisterActivity : AppCompatActivity() {

    private lateinit var mButtonRegister: Button
    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var mSSN: EditText
    private lateinit var mEmail: EditText
    private lateinit var mMobile: EditText
    private lateinit var mLocationCode: EditText
    private lateinit var mLocation: EditText
    private lateinit var mAddress: EditText
    private lateinit var mExp: EditText
    private lateinit var mPassword: EditText

    companion object {
        const val TAG: String = "RegisterActivity"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirstName = findViewById<EditText>(R.id.firstName)
        mLastName = findViewById<EditText>(R.id.lastName)
        mSSN = findViewById<EditText>(R.id.ssn)
        mEmail = findViewById<EditText>(R.id.email_field)
        mMobile = findViewById<EditText>(R.id.mobile_field)
        mLocationCode = findViewById<EditText>(R.id.locationCode)
        mLocation = findViewById<EditText>(R.id.location)
        mAddress = findViewById<EditText>(R.id.address)
        mExp = findViewById<EditText>(R.id.experience)
        mPassword = findViewById<EditText>(R.id.password_field)

        mButtonRegister = findViewById<Button>(R.id.register_button)
        mButtonRegister.setOnClickListener {
            val allInputsValid = checkRequiredFields();

            if (allInputsValid) {
                // call network register function
            }
        }
    }

    private fun checkRequiredFields(): Boolean {
        val requiredFields = arrayOf(
            R.id.firstName,
            R.id.lastName,
            R.id.ssn,
            R.id.email_field,
            R.id.mobile_field,
            R.id.locationCode,
            R.id.location,
            R.id.address,
            R.id.experience,
            R.id.password_field
        )

        var isValid = true;
        for (id in requiredFields) {
            val field = findViewById<EditText>(id);
            if (field.text.isEmpty()) {
                field.error = "Verður að fylla út"
                isValid = false;
            }
        }
        return isValid
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