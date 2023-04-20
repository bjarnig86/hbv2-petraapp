package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.ChildDTO
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapter
import `is`.hi.hbv601g.petraapp.fragments.BottomNav
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager


class DcwActivity : AppCompatActivity(){
    
    private lateinit var ChildList: ArrayList<Child>
    private lateinit var mNoChildren: TextView
    private val bottomNav = BottomNav()

    companion object {
        const val TAG: String = "DcwActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dcw)

        val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val dcwId = prefs.getString("DCW_ID", "")

        bottomNav.handleFragments(this, supportFragmentManager, R.id.bottom_nav)
        mNoChildren = findViewById(R.id.no_children_text_view)

        val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView

        val networkManager = NetworkManager.getInstance(this)
        Log.d(TAG, "onCreate: dcwId = ${dcwId}")
        networkManager.getChildrenByDCW(dcwId!!, object: NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {
                ChildList = result as ArrayList<Child>

                if (ChildList.isEmpty()) {
                    mNoChildren.visibility = View.VISIBLE
                    childRecyclerView.visibility = View.GONE
                }
                val adapter = ChildAdapter(ChildList,this@DcwActivity);
                childRecyclerView.adapter = adapter;
                childRecyclerView.layoutManager = LinearLayoutManager(this@DcwActivity);
            }

            override fun onFailure(errorString: String) {
                Log.e(TAG, "onFailure: $errorString", )
            }

        })


    }
}

