package `is`.hi.hbv601g.petraapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.adapters.ChildAdapter


class DcwActivity : AppCompatActivity(){
    
    private lateinit var ChildList: ArrayList<Child>

    companion object {
        const val TAG: String = "DcwActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dcw)

        val childRecyclerView = findViewById<View>(R.id.rvChildren) as RecyclerView

        ChildList = arrayListOf(
            Child(
                firstName = "Einar",
                lastName = "Pálsson",
                ssn = 5812345
            ),
            Child(
                firstName = "Bjarni",
                lastName = "Guðmundsson",
                ssn = 5552345
            ),
            Child(
                firstName = "Dagbjört",
                lastName = "Þorgrímsdóttir",
                ssn = 5812345
            ),

        );

        val adapter = ChildAdapter(ChildList,this);

        childRecyclerView.adapter = adapter;

        childRecyclerView.layoutManager = LinearLayoutManager(this);

    }
}

