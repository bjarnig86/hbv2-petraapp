package `is`.hi.hbv601g.petraapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import `is`.hi.hbv601g.petraapp.DayReportActivity
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.Entities.ChildDTO
import `is`.hi.hbv601g.petraapp.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class ChildAdapter(private val mChild: ArrayList<Child>, private val context: Context) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val icon = itemView.findViewById<ImageView>(R.id.baby_icon_image_view)

        val nameTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_name_content);

        val ssnTitleViewContent = itemView.findViewById<TextView>(R.id.child_card_list_ssn_content);

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
        val child: Child = mChild[position]

        val icon = viewHolder.icon
        val currentDate = Date()

        val title = viewHolder.titleView
        title.text = child.firstName

        val name = viewHolder.nameTitleViewContent
        name.text = child.firstName

        val ssn = viewHolder.ssnTitleViewContent
        var ssnString = ""
        if (child.ssn.length == 10) {
            ssnString = child.ssn.substring(0,6) + "-" + child.ssn.substring(6,10)
            ssn.text = ssnString
        } else {
            ssn.text = child.ssn
        }

        if (child.sicknessDay?.let { compareDates(currentDate, it) } == true) {
            icon.setImageResource(R.drawable.sick_outline_rounded)
            title.setTextColor(ContextCompat.getColor(context, R.color.sickness_color))
        } else {
            icon.setImageResource(R.drawable.mood_kid)
            title.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        val reportCreate = viewHolder.createReport
        reportCreate.setOnClickListener{
            val intent = Intent(context, DayReportActivity::class.java)

            // Pass data to SecondActivity (optional)
            val childName = "${mChild[position].firstName} ${mChild[position].lastName}"
            intent.putExtra("childName", childName)
            intent.putExtra("childId", child.id)
            startActivity(context, intent, null)
        }

        val reportsGet = viewHolder.getReport
        reportsGet.setOnClickListener{
            Toast.makeText(context,"Sækja skýrlsu", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return mChild.size
    }

    fun compareDates(date1: Date, date2: Date): Boolean {
        // Create a SimpleDateFormat object to format the date in "yyyy-MM-dd" format
        val utcTimeZone = TimeZone.getTimeZone("GMT+0")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = utcTimeZone

        // Convert the Date objects to strings in the desired format
        val dateString1 = dateFormat.format(date1)
        val dateString2 = dateFormat.format(date2)

        // Compare the two date strings and return the result
        return dateString1 == dateString2
    }
}