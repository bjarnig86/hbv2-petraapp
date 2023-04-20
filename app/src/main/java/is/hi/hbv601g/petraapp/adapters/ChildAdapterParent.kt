package `is`.hi.hbv601g.petraapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.ViewDayReportsActivity
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import java.text.SimpleDateFormat
import java.util.*


class ChildAdapterParent(private val mChild: List<Child>, private val context: Context) : RecyclerView.Adapter<ChildAdapterParent.ViewHolder>() {
    companion object {
        const val TAG = "ChildAdapterParent"
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val icon = itemView.findViewById<ImageView>(R.id.child_card_icon)

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
        val currentDate = Date()
        val icon = viewHolder.icon

        val title = viewHolder.titleView
        title.text = child.firstName

        val name = viewHolder.nameTitleViewContent
        name.text = child.firstName

        val ssn = viewHolder.ssnTitleViewContent
        val ssnString = child.ssn.substring(0,6) + "-" + child.ssn.substring(6,10)
        ssn.text = ssnString

        val notifySickness = viewHolder.notifySickness

        if (child.sicknessDay?.let { compareDates(currentDate, it) } == true) {
            icon.setImageResource(R.drawable.sick_outline_rounded)
            notifySickness.setTextColor(Color.LTGRAY)
            notifySickness.isEnabled = false
            title.setTextColor(ContextCompat.getColor(context, R.color.sickness_color))
        } else {
            icon.setImageResource(R.drawable.mood_kid)
            notifySickness.isEnabled = true
            notifySickness.setTextColor(ContextCompat.getColor(context, R.color.primary))
            title.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        notifySickness.setOnClickListener{
            val builder = AlertDialog.Builder(context)

            // Set the title and message for the dialog
            builder.setTitle("Tilkynna veikindi hjá ${child.firstName}")
                .setMessage("Staðfesta veikindi barns?")

            // Set the Yes button and its action
            builder.setPositiveButton("Já") { dialog, which ->
                // Send the request and go back to the activity
                networkManager.notifySickLeave(child.id, object : NetworkCallback<String> {
                override fun onSuccess(result: String) {
                    icon.setImageResource(R.drawable.sick_outline_rounded)
                    notifySickness.setTextColor(Color.LTGRAY)
                    notifySickness.isEnabled = false
                    title.setTextColor(ContextCompat.getColor(context, R.color.sickness_color))
                    Log.d(TAG, "onSuccess: Notifying Sick Leave SUCCESSFUL")
                    Toast.makeText(context, "Veikindi tilkynnt!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorString: String) {
                    Toast.makeText(context, "VILLA!", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: $errorString")
                }

            })
                dialog.dismiss()
            }

            // Set the Cancel button and its action
            builder.setNegativeButton("Hætta við") { dialog, which ->
                // Just go back to the activity
                dialog.dismiss()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()


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