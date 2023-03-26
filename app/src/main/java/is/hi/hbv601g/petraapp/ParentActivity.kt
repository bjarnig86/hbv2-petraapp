package `is`.hi.hbv601g.petraapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapterParent

class ParentActivity : AppCompatActivity() {
    private lateinit var ChildList: ArrayList<Child>
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

        val adapter = ChildAdapterParent(ChildList,this);

        childRecyclerView.adapter = adapter;

        childRecyclerView.layoutManager = LinearLayoutManager(this);

    }
}