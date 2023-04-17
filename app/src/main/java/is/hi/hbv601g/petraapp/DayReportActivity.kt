package `is`.hi.hbv601g.petraapp

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import `is`.hi.hbv601g.petraapp.Entities.DayReport
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.fragments.BottomNav
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import java.text.SimpleDateFormat
import java.util.*

class DayReportActivity : AppCompatActivity() {

    private lateinit var mChildName: TextView
    private lateinit var mDateTo: EditText
    private lateinit var mDateFrom: EditText
    private lateinit var mComment: EditText;
    private lateinit var mSubmitReport: Button
    companion object {
        const val TAG: String = "DayReportActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dayreport)

        val bundle = intent.extras
        mChildName = findViewById<EditText>(R.id.child_name)

        val childName = bundle?.getString("childName")

        if (!childName.isNullOrEmpty()) {
            mChildName.text = childName
        }

        val childId = bundle?.getLong("childId")


        mDateFrom = findViewById(R.id.report_sleep_from)
        mDateFrom.inputType = InputType.TYPE_NULL
        mDateFrom.setOnClickListener {
            promptTimePicker("timePicker_From", mDateFrom)
        }

        mDateTo = findViewById(R.id.report_sleep_to)
        mDateTo.inputType = InputType.TYPE_NULL
        mDateTo.setOnClickListener {
            promptTimePicker("timePicker_To", mDateTo)
        }

        val spinner: Spinner = findViewById(R.id.report_appetite)
        val options = arrayOf("Matarlyst:", "Vond", "Sæmileg", "Góð", "Mjög góð")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = adapter

        mSubmitReport = findViewById(R.id.report_register)
        mSubmitReport.setOnClickListener {
            if (mDateFrom.text.isEmpty() || mDateTo.text.isEmpty()) {
                Toast.makeText(this,"Verður að fylla inn í báða 'Svefn' reitina", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sleepFrom = convertToProperDate(mDateFrom.text.toString())
            val sleepTo = convertToProperDate(mDateTo.text.toString())

            if (sleepFrom > sleepTo) {
                Toast.makeText(this,"'Svefn frá' má ekki byrja á eftir 'svefn til'", Toast.LENGTH_SHORT).show()
            }

            if (spinner.selectedItem.toString() === "Matarlyst:") {
                Toast.makeText(this,"Verður að velja matarlyst", Toast.LENGTH_SHORT).show()
            }

            val dcwId = User.id!!;

            val dayReport = DayReport(sleepFrom, sleepTo, spinner.selectedItem.toString(), mComment.text.toString(), dcwId, childId!!);

            val nm = NetworkManager.getInstance(this)
            nm.createDayReport(dayReport, object: NetworkCallback<DayReport>{
                override fun onSuccess(result: DayReport) {
                    Toast.makeText(this@DayReportActivity,"Gleðilega skýrslu", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorString: String) {
                    Toast.makeText(this@DayReportActivity,"Úbbsí, eitthvað fór úrskeiðis", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun promptTimePicker(tag:String, element: EditText) {
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = timeFormat.format(calendar.time)

            element.setText(formattedTime)
        }

        timePicker.show(supportFragmentManager, tag)
    }

    private fun convertToProperDate(str: String) : Date {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val strToDate = timeFormat.parse(str)

        calendar.time = strToDate
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        return calendar.time
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }
}