package `is`.hi.hbv601g.petraapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.DayReportDTO
import `is`.hi.hbv601g.petraapp.R

class ReportAdapter(private val reports: ArrayList<DayReportDTO>, private val childName: String) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val childNameTextView: TextView = itemView.findViewById(R.id.childNameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val sleepFromTextView: TextView = itemView.findViewById(R.id.sleepFromTextView)
        private val sleepToTextView: TextView = itemView.findViewById(R.id.sleepToTextView)
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        private val appetiteTextView: TextView = itemView.findViewById(R.id.appetiteTextView)

        fun bind(report: DayReportDTO) {
            childNameTextView.text = childName
            dateTextView.text = report.date.toString()
            sleepFromTextView.text = report.sleepFrom.toString()
            sleepToTextView.text = report.sleepTo.toString()
            commentTextView.text = report.comment
            appetiteTextView.text = report.appetite
        }
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