package `is`.hi.hbv601g.petraapp

import android.os.Bundle
import android.util.Log
import android.view.View
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
    private val bottomNav = BottomNav()

    companion object {
        const val TAG: String = "DcwActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dcw)

        bottomNav.handleFragments(this, supportFragmentManager, R.id.bottom_nav)

        val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView

        val networkManager = NetworkManager.getInstance(this)
        networkManager.getChildrenByDCW(User.id!!, object: NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {
                Log.d(TAG, "onSuccess: ${result[0].daycare_worker_id} dcwId")
                ChildList = result as ArrayList<Child>
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

