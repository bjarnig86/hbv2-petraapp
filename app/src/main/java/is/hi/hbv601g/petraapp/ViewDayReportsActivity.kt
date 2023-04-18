package `is`.hi.hbv601g.petraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.DayReport
import `is`.hi.hbv601g.petraapp.Entities.DayReportDTO
import `is`.hi.hbv601g.petraapp.adapters.ReportAdapter
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class ViewDayReportsActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mMessage: TextView
    private var mChildId: Int = 0
    private var mChildName: String = ""

    companion object {
        const val TAG: String = "ViewDayReportsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_day_reports)
        val bundle = intent.extras
        if (bundle != null) {
            mChildId = bundle.getInt("childId")
            mChildName = bundle.getString("childName").toString()
        }

        mMessage = findViewById(R.id.reports_empty_text_view)
        mProgressBar = findViewById(R.id.progress_bar)
        mRecyclerView = findViewById(R.id.recyclerView_DayReports)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val networkManager = NetworkManager.getInstance(this)
        networkManager.getDayReportsByChild(mChildId, object : NetworkCallback<ArrayList<DayReportDTO>> {
            override fun onSuccess(result: ArrayList<DayReportDTO>) {
                if (result.isEmpty()) {
                    mProgressBar.visibility = View.GONE
                    mMessage.visibility = View.VISIBLE
                } else {
                    mRecyclerView.visibility = View.VISIBLE
                    mRecyclerView.adapter = ReportAdapter(result, mChildName)
                    Log.d(TAG, "onSuccess: $result")

                    mProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(errorString: String) {
                Log.e(TAG, "onFailure: Something went wrong: $errorString", )
                //TODO("Not yet implemented")
            }

        })



    }
}