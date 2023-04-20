package `is`.hi.hbv601g.petraapp.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.Appetite
import `is`.hi.hbv601g.petraapp.Entities.DayReportDTO
import `is`.hi.hbv601g.petraapp.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReportAdapter(private val reports: ArrayList<DayReportDTO>, private val childName: String) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextViewDay: TextView = itemView.findViewById(R.id.dateTextView_day)
        private val dateTextViewNum: TextView = itemView.findViewById(R.id.dateTextView_num)
        private val dateTextViewMonth: TextView = itemView.findViewById(R.id.dateTextView_month)

        private val childNameTextView: TextView = itemView.findViewById(R.id.childNameTextView)
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)

        private val sleepFromTextView: TextView = itemView.findViewById(R.id.sleepFromToTextView)

        private val appetiteTextView: TextView = itemView.findViewById(R.id.appetiteTextView)

        fun bind(report: DayReportDTO) {
            dateTextViewDay.text = getDayOfWeek(report.date);
            dateTextViewNum.text = getDate(report.date);
            dateTextViewMonth.text = getMonth(report.date)

            val reportIdx = "Skýrsla - ${reports.indexOf(report)+1}"
            childNameTextView.text = reportIdx

            commentTextView.text = report.comment

            val sleepStr = "${getTimeIn24HourFormat(report.sleepFrom)} - ${getTimeIn24HourFormat(report.sleepTo)}"
            sleepFromTextView.text = sleepStr

            appetiteTextView.text = getAppetite(report.appetite)
        }
    }

    fun getAppetite(appetite: String): String {
        return when (appetite) {
            "BAD" -> "Ekki vel"
            "OKAY" -> "Ágætlega"
            "GOOD" -> "Vel"
            "VERY_GOOD" -> "Mjög vel"
            else -> { "Óskráð" }
        }
    }

    // Function to extract the day in text form (e.g. "Tue")
    fun getDayOfWeek(date: Date): String {
        val sdf = SimpleDateFormat("EEE", Locale.US)
        return sdf.format(date)
    }

    // Function to extract the date (e.g. "18")
    fun getDate(date: Date): String {
        val sdf = SimpleDateFormat("dd", Locale.US)
        return sdf.format(date)
    }

    // Function to extract the month in text form (e.g. "Nov")
    fun getMonth(date: Date): String {
        val sdf = SimpleDateFormat("MMM", Locale.US)
        return sdf.format(date)
    }

    // Function to extract the time in 24-hour format (e.g. "19:00")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeIn24HourFormat(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(formatter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_day_report, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport = reports[position]
        holder.bind(currentReport)
    }

    override fun getItemCount() = reports.size
}