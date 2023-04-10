package `is`.hi.hbv601g.petraapp

import android.app.Dialog
import android.content.Context
<<<<<<< Updated upstream
import android.content.Intent
=======
>>>>>>> Stashed changes
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
<<<<<<< Updated upstream
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
=======
>>>>>>> Stashed changes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class CreateChildFragment : DialogFragment() {

    private lateinit var mCreateChildButton: Button
<<<<<<< Updated upstream
    private lateinit var mCancelButton: ImageButton
=======
    private lateinit var mCancelButton: Button
>>>>>>> Stashed changes
    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var mSSN: EditText
    private lateinit var mNetworkManager: NetworkManager

    companion object {
        const val TAG: String = "CreateChildFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize non-UI related code here, if necessary
        mNetworkManager = NetworkManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_create_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
        val preferences = requireContext().getSharedPreferences("MY_APP_PREFS",
            Context.MODE_PRIVATE)

        mCreateChildButton = view.findViewById(R.id.createChildButton)
<<<<<<< Updated upstream
        mCancelButton = view.findViewById(R.id.close_button)
=======
        mCancelButton = view.findViewById(R.id.dismiss_button)
>>>>>>> Stashed changes
        mFirstName = view.findViewById(R.id.childsFirstName)
        mLastName = view.findViewById(R.id.childsLastName)
        mSSN = view.findViewById(R.id.ssn)

        val gson = Gson()
        val json: String? = preferences.getString("USER_KEY", "children")
        val parent: Parent = gson.fromJson(json, Parent::class.java)

        mCreateChildButton.setOnClickListener{
<<<<<<< Updated upstream
            if (mSSN.text.isBlank() || mFirstName.text.isBlank() || mLastName.text.isBlank()) {
                val errorTextView = view.findViewById<TextView>(R.id.error_msg)
                errorTextView.text = this.getString(R.string.create_child_error)
            } else {
                val child = Child(mSSN.text.toString(), mFirstName.text.toString(), mLastName.text.toString())
                mNetworkManager.createChild(child, parent, object: NetworkCallback<Child>{
                    override fun onSuccess(result: Child) {
                        Log.d(TAG, "onSuccess: $result")
                        dismiss()
                        val intent = Intent(requireContext(), ParentActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(errorString: String) {
                        Log.e(TAG, "onFailure: createChild: $errorString", )
                    }
                })
            }
=======
            val child = Child(mSSN.text.toString(), mFirstName.text.toString(), mLastName.text.toString())
            mNetworkManager.createChild(child, parent, object: NetworkCallback<Child>{
                override fun onSuccess(result: Child) {
                    Log.d(TAG, "onSuccess: $result")
                    dismiss()
                }

                override fun onFailure(errorString: String) {
                    Log.e(TAG, "onFailure: createChild: $errorString", )
                }
            })
>>>>>>> Stashed changes
        }

        mCancelButton.setOnClickListener{
            dismiss()
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