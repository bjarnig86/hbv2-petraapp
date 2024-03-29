package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapterParent
import `is`.hi.hbv601g.petraapp.databinding.ActivityParentBinding
import `is`.hi.hbv601g.petraapp.fragments.BottomNavLoggedIn
import `is`.hi.hbv601g.petraapp.fragments.CreateChildFragment
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager


class ParentActivity : AppCompatActivity() {
    private lateinit var mChildren: ArrayList<Child>
    private lateinit var mCreateNewChild: Button
    private lateinit var mParentId: String
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mChildrenRecyclerView: RecyclerView
    private lateinit var mNoChildren: TextView

    private lateinit var binding: ActivityParentBinding
    companion object {
        const val TAG: String = "ParentActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mProgressBar = findViewById(R.id.progress_bar)
        mChildrenRecyclerView = findViewById(R.id.rvChildren)
        mNoChildren = findViewById(R.id.no_children_text_view)

        // do it this way because you cannot be on this page unless logged in
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.bottom_nav, BottomNavLoggedIn())
        fragmentTransaction.commit()

        val preferences = getSharedPreferences("MY_APP_PREFS",
            Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = preferences.getString("USER_KEY", "children")
        val obj: Parent = gson.fromJson(json, Parent::class.java)
        mParentId = obj.id.toString()

        val nm = NetworkManager.getInstance(this)

        nm.getChildrenByParent(mParentId.toLong(), object: NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {
                mChildren = result as ArrayList<Child>

                if (mChildren.isEmpty()) {
                    mNoChildren.visibility = View.VISIBLE
                    mProgressBar.visibility = View.GONE
                } else {
                    val adapter = ChildAdapterParent(mChildren,this@ParentActivity)
                    val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView
                    childRecyclerView.adapter = adapter
                    childRecyclerView.layoutManager = LinearLayoutManager(this@ParentActivity)
                    mProgressBar.visibility = View.GONE
                    mChildrenRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(errorString: String) {
                Log.e(TAG, "onFailure: onResume: $errorString")
            }
        })


        mCreateNewChild = findViewById(R.id.newChildButton)
        mCreateNewChild.setOnClickListener{
            val createChildFragment = CreateChildFragment()
            createChildFragment.show(supportFragmentManager, "ble")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(CreateChildFragment.TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        Log.d(CreateChildFragment.TAG, "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        Log.d(CreateChildFragment.TAG, "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
        Log.d(CreateChildFragment.TAG, "onStop: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        Log.d(CreateChildFragment.TAG, "onPause: ")
    }
}