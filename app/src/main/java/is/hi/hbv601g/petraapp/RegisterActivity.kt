package `is`.hi.hbv601g.petraapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import `is`.hi.hbv601g.petraapp.Entities.*
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var mButtonRegister: Button
    private lateinit var mButtonDCWOption: Button
    private lateinit var mButtonParentOption: Button
    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var mSSN: EditText
    private lateinit var mEmail: EditText
    private lateinit var mMobile: EditText
    private lateinit var mLocation: AutoCompleteTextView
    private lateinit var mAddress: EditText
    private lateinit var mExp: EditText
    private lateinit var mPassword: EditText
    val networkManager = NetworkManager.getInstance(this)


    companion object {
        const val TAG: String = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        mFirstName = findViewById(R.id.firstName)
        mLastName = findViewById(R.id.lastName)
        mSSN = findViewById(R.id.ssn)
        mEmail = findViewById(R.id.email_field)
        mMobile = findViewById(R.id.mobile_field)
        mButtonDCWOption = findViewById(R.id.dcw_option_button)
        mButtonParentOption = findViewById(R.id.parent_option_button)

        var option = "DCW"

        // dynamic locations
        val sharedPreferences = this.getSharedPreferences(
            "MY_APP_PREFS",
            Context.MODE_PRIVATE)
        val locations = sharedPreferences.getStringSet("LOCATIONS", emptySet())


        mLocation = findViewById(R.id.location)
        val locationCodeAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locations?.toList()!!)
        mLocation.setAdapter(locationCodeAdapter)
        mLocation.validator = object : AutoCompleteTextView.Validator {
            override fun isValid(text: CharSequence?): Boolean {
                // Check if the entered value is in the list of valid values
                return locations.contains(text.toString())
            }

            override fun fixText(invalidText: CharSequence?): CharSequence {
                // Since we only allow valid values, there's no need to fix invalid text
                Toast.makeText(this@RegisterActivity, "Verður að velja rétt póstnúmer", Toast.LENGTH_SHORT).show()
                return ""
            }
        }
        mLocation.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                locationCodeAdapter.filter.filter(p0)

            }

            override fun afterTextChanged(p0: Editable?) {
                if (mLocation.text.isBlank()) {
                    this@RegisterActivity.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            }

        })




        mAddress = findViewById(R.id.address)
        mExp = findViewById(R.id.experience)
        mPassword = findViewById(R.id.password_field)

        mButtonRegister = findViewById(R.id.register_button)

        mButtonParentOption.setOnClickListener {
            option = "Parent"
            mExp.visibility = View.GONE
            mAddress.visibility = View.GONE
            mLocation.visibility = View.GONE
        }

        mButtonDCWOption.setOnClickListener {
            option = "DCW"
            mExp.visibility = View.VISIBLE
            mAddress.visibility = View.VISIBLE
            mLocation.visibility = View.VISIBLE
        }

        mButtonRegister.setOnClickListener {
            if (option == "DCW") {
                val allInputsValid = checkRequiredFields(option);
                if (allInputsValid) {
                    val dcw = DaycareWorkerDTO(
                        mFirstName.text.toString(),
                        mLastName.text.toString(),
                        mEmail.text.toString(),
                        mSSN.text.toString(),
                        mAddress.text.toString(),
                        mLocation.text.toString().split(" ")[1],
                        mLocation.text.toString().split(" ")[0],
                        mExp.text.toString().toInt(),
                        mMobile.text.toString(),
                        mPassword.text.toString()
                    )

                    networkManager.addDaycareWorker(dcw, object : NetworkCallback<DaycareWorker> {
                        override fun onSuccess(result: DaycareWorker) {
                            val intent = Intent()
                            intent.putExtras(intent)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(errorString: String) {
                            Log.e(CreateChildFragment.TAG, "onFailure: createChild: $errorString")
                        }
                    })
                }
            } else {
                val allInputsValid = checkRequiredFields(option);
                if (allInputsValid) {
                    val parent = ParentDTO(
                        mSSN.text.toString(),
                        mFirstName.text.toString(),
                        mLastName.text.toString(),
                        mEmail.text.toString(),
                        mMobile.text.toString(),
                        mPassword.text.toString()
                    )

                    networkManager.addParent(parent, object : NetworkCallback<Parent> {
                        override fun onSuccess(result: Parent) {
                            val intent = Intent()
                            intent.putExtras(intent)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(errorString: String) {
                            Log.e(CreateChildFragment.TAG, "onFailure: createChild: $errorString")
                        }
                    })
                }
            }
        }
    }
    private fun checkRequiredFields(option: String): Boolean {
        val requiredFields = arrayOf(
            R.id.firstName,
            R.id.lastName,
            R.id.ssn,
            R.id.email_field,
            R.id.mobile_field,
            R.id.password_field
        )
        val requiredFieldsDCW = arrayOf(
            R.id.location,
            R.id.address,
            R.id.experience
        )
        var isValid = true;
        for (id in requiredFields) {
            val field = findViewById<EditText>(id);
            if (field.text.isEmpty()) {
                field.error = "Verður að fylla út"
                isValid = false;
            }
        }
        if (option == "DCW") {
            for (id in requiredFieldsDCW) {
                val field = findViewById<EditText>(id);
                if (field.text.isEmpty()) {
                    field.error = "Verður að fylla út"
                    isValid = false;
                }
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