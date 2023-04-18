package `is`.hi.hbv601g.petraapp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import `is`.hi.hbv601g.petraapp.DayReportActivity
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.ViewDayReportsActivity
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager


class ChildAdapterParent(private val mChild: List<Child>, private val context: Context) : RecyclerView.Adapter<ChildAdapterParent.ViewHolder>() {
    companion object {
        const val TAG = "ChildAdapterParent"
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val subTitleView = itemView.findViewById<TextView>(R.id.card_sub_title)

        val nameTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_name_content);

        val ssnTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_ssn_content);

        val notifySickness = itemView.findViewById<MaterialButton>(R.id.child_card_notify_sickness)

        val getReports = itemView.findViewById<MaterialButton>(R.id.child_card_get_reports)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAdapterParent.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val childView = inflater.inflate(R.layout.childcard_parent, parent, false)
        return ViewHolder(childView)
    }

    override fun onBindViewHolder(viewHolder: ChildAdapterParent.ViewHolder, position: Int) {
        val networkManager = NetworkManager.getInstance(context)
        val child: Child = mChild[position]
        val title = viewHolder.titleView
        title.text = child.firstName

        val name = viewHolder.nameTitleViewContent
        name.text = child.firstName

        val ssn = viewHolder.ssnTitleViewContent
        ssn.text = child.ssn

        val notifySickness = viewHolder.notifySickness
        notifySickness.setOnClickListener{
            networkManager.notifySickLeave(child.id, object : NetworkCallback<String> {
                override fun onSuccess(result: String) {
                    Log.d(TAG, "onSuccess: Notifying Sick Leave SUCCESSFUL")
                    Toast.makeText(context, "Veikindi tilkynnt!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorString: String) {
                    Toast.makeText(context, "VILLA!", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: $errorString")
                }

            })
            Toast.makeText(context,"Veikindi tilkynnt", Toast.LENGTH_SHORT).show()
        }

        val getReports = viewHolder.getReports
        getReports.setOnClickListener {
            val intent = Intent(context, ViewDayReportsActivity::class.java)
            intent.putExtra("childId", child.id)
            intent.putExtra("childName", child.firstName + " " + child.lastName)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return mChild.size
    }
}