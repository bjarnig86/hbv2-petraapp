package `is`.hi.hbv601g.petraapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.R


class ChildAdapter(private val mChild: List<Child>,private val context : Context) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val subTitleView = itemView.findViewById<TextView>(R.id.card_sub_title)

        val nameTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_name_content);

        val ssnTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_ssn_content);

        val alertBtn = itemView.findViewById<ImageButton>(R.id.danger_button);

        val createReport = itemView.findViewById<MaterialButton>(R.id.child_card_create_report)

        val getReport = itemView.findViewById<MaterialButton>(R.id.child_card_get_reports)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val childView = inflater.inflate(R.layout.childcard, parent, false)
        return ViewHolder(childView)
    }

    override fun onBindViewHolder(viewHolder: ChildAdapter.ViewHolder, position: Int) {
        val child: Child = mChild.get(position)
        val title = viewHolder.titleView
        title.text = child.firstName

        val name = viewHolder.nameTitleViewContent
        name.text = child.firstName

        val ssn = viewHolder.ssnTitleViewContent
        ssn.text = child.ssn.toString()

        val alertBtn = viewHolder.alertBtn
        alertBtn.setOnClickListener {
            Toast.makeText(context,"HÆTTA!", Toast.LENGTH_SHORT).show()
        }

        val reportCreate = viewHolder.createReport
        reportCreate.setOnClickListener{
            Toast.makeText(context,"Skýrlsa", Toast.LENGTH_SHORT).show()
        }

        val reportsGet = viewHolder.getReport
        reportsGet.setOnClickListener{
            Toast.makeText(context,"Sækja skýrlsu", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return mChild.size
    }
}