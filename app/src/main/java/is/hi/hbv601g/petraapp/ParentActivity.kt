package `is`.hi.hbv601g.petraapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
<<<<<<< Updated upstream
=======
import androidx.fragment.app.DialogFragment
>>>>>>> Stashed changes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapterParent
import `is`.hi.hbv601g.petraapp.databinding.ActivityParentBinding
import `is`.hi.hbv601g.petraapp.fragments.BottomNavLoggedIn
<<<<<<< Updated upstream
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
=======
>>>>>>> Stashed changes


class ParentActivity : AppCompatActivity() {
    private lateinit var ChildList: ArrayList<Child>
    private lateinit var mCreateNewChild: Button
    private lateinit var mParentId: String
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mChildrenRecyclerView: RecyclerView

    lateinit var binding: ActivityParentBinding
    companion object {
        const val TAG: String = "ParentActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mProgressBar = findViewById(R.id.progress_bar)
        mChildrenRecyclerView = findViewById(R.id.rvChildren)

        // do it this way because you cannot be on this page unless logged in
        val fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav, BottomNavLoggedIn());
        fragmentTransaction.commit();

        val preferences = getSharedPreferences("MY_APP_PREFS",
            Context.MODE_PRIVATE);
        val gson = Gson()
        val json: String? = preferences.getString("USER_KEY", "children")
        val obj: Parent = gson.fromJson(json, Parent::class.java)
        mParentId = obj.id.toString()

        val nm = NetworkManager.getInstance(this)

        nm.getChildrenByParent(mParentId, object: NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {
                ChildList = result as ArrayList<Child>
                val adapter = ChildAdapterParent(ChildList,this@ParentActivity);
                val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView
                childRecyclerView.adapter = adapter;
                childRecyclerView.layoutManager = LinearLayoutManager(this@ParentActivity);
                mProgressBar.visibility = View.GONE
                mChildrenRecyclerView.visibility = View.VISIBLE
            }

            override fun onFailure(errorString: String) {
//                TODO("Not yet implemented")
                Log.e(TAG, "onFailure: onResume: $errorString", )
            }

        })


        mCreateNewChild = findViewById(R.id.newChildButton)
        mCreateNewChild.setOnClickListener{
//            val intent = Intent(this, CreateChildActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
            val createChildFragment = CreateChildFragment()
            createChildFragment.show(supportFragmentManager, "ble")
        }

//        val adapter = ChildAdapterParent(ChildList,this);
//
//        childRecyclerView.adapter = adapter;
//
//        childRecyclerView.layoutManager = LinearLayoutManager(this);

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(CreateChildFragment.TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
<<<<<<< Updated upstream
        Log.d(TAG, "onResume: ")
=======
        Log.d(CreateChildFragment.TAG, "onResume: ")
>>>>>>> Stashed changes
    }

    override fun onDestroy() {
        super.onDestroy()
<<<<<<< Updated upstream
        Log.d(TAG, "onDestroy: ")
=======
        Log.d(CreateChildFragment.TAG, "onDestroy: ")
>>>>>>> Stashed changes
    }

    override fun onStop() {
        super.onStop()
<<<<<<< Updated upstream
        Log.d(TAG, "onStop: ")
=======
        Log.d(CreateChildFragment.TAG, "onStop: ")
>>>>>>> Stashed changes
    }

    override fun onPause() {
        super.onPause()
<<<<<<< Updated upstream
        Log.d(TAG, "onPause: ")
=======
        Log.d(CreateChildFragment.TAG, "onPause: ")
>>>>>>> Stashed changes
    }
}