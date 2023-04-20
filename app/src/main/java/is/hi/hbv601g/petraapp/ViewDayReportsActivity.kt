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

    private lateinit var mTitle: TextView //report_view_title
    private lateinit var mSubtitle: TextView //report_view_subtitle
    private lateinit var mDcwName: TextView // child_report_dcw_name
    private lateinit var mDcwPhone: TextView //child_report_dcw_phone
    private lateinit var mDcwEmail: TextView //child_report_dcw_email

    private var mChildId: Int = 0
    private var mChildName: String = ""
    private var mChildSsn: String = ""

    private var mDcwNameString: String = ""
    private var mDcwMobile: String = ""
    private var mDcwEmailString: String = ""

    companion object {
        const val TAG: String = "ViewDayReportsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_day_reports)
        val bundle = intent.extras
        if (bundle != null) {
            // Get child attributes
            mChildId = bundle.getInt("childId")
            mChildName = bundle.getString("childName").toString()
            mChildSsn = bundle.getString("childSsn").toString()

            // Get dcw attributes
            mDcwNameString = bundle.getString("dcwName").toString()
            mDcwMobile = bundle.getString("dcwMobile").toString()
            mDcwEmailString = bundle.getString("dcwEmail").toString()
        }

        mMessage = findViewById(R.id.reports_empty_text_view)
        mProgressBar = findViewById(R.id.progress_bar)
        mRecyclerView = findViewById(R.id.recyclerView_DayReports)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mTitle = findViewById(R.id.report_view_title)
        mTitle.text = mChildName

        mSubtitle = findViewById(R.id.report_view_subtitle)
        mSubtitle.text = mChildSsn

        mDcwName = findViewById(R.id.child_report_dcw_name)
        mDcwName.text = mDcwNameString

        mDcwPhone = findViewById(R.id.child_report_dcw_phone)
        mDcwPhone.text = mDcwMobile

        mDcwEmail = findViewById(R.id.child_report_dcw_email)
        mDcwEmail.text = mDcwEmailString

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