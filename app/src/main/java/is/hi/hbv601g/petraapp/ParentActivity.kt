package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapterParent
import `is`.hi.hbv601g.petraapp.adapters.DaycareWorkerCardAdapter

class ParentActivity : AppCompatActivity() {
    private lateinit var ChildList: ArrayList<Child>
    private lateinit var mCreateNewChild: Button

    companion object {
        const val TAG: String = "ParentActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView

        ChildList = arrayListOf(
            Child(
                firstName = "Kid1",
                lastName = "Surname",
                ssn = "5812300"
            ),
            Child(
                firstName = "Kid2",
                lastName = "Surname",
                ssn = "5552300"
            )
            );

        mCreateNewChild = findViewById(R.id.newChildButton)
        mCreateNewChild.setOnClickListener {
            Toast.makeText(this@ParentActivity, "Create new child!", Toast.LENGTH_SHORT).show()
        }

        val adapter = ChildAdapterParent(ChildList,this);

        childRecyclerView.adapter = adapter;

        childRecyclerView.layoutManager = LinearLayoutManager(this);

    }
}