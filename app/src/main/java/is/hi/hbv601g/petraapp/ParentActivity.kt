package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapterParent
import `is`.hi.hbv601g.petraapp.databinding.ActivityParentBinding
import `is`.hi.hbv601g.petraapp.fragments.BottomNavLoggedIn


class ParentActivity : AppCompatActivity() {
    private lateinit var ChildList: ArrayList<Child>
    private lateinit var mCreateNewChild: Button
    lateinit var binding: ActivityParentBinding
    companion object {
        const val TAG: String = "ParentActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_nav, BottomNavLoggedIn());
        fragmentTransaction.commit();

        val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView
        val preferences = getSharedPreferences("MY_APP_PREFS",
            Context.MODE_PRIVATE);
        val gson = Gson()
        val json: String? = preferences.getString("USER_KEY", "children")
        val obj: Parent = gson.fromJson(json, Parent::class.java)

        ChildList = obj.children as ArrayList<Child>;

        mCreateNewChild = findViewById(R.id.newChildButton)
        mCreateNewChild.setOnClickListener {
            Toast.makeText(this@ParentActivity, "Create new child!", Toast.LENGTH_SHORT).show()
        }

        val adapter = ChildAdapterParent(ChildList,this);

        childRecyclerView.adapter = adapter;

        childRecyclerView.layoutManager = LinearLayoutManager(this);

    }
}