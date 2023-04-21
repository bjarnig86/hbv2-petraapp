package `is`.hi.hbv601g.petraapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import `is`.hi.hbv601g.petraapp.Entities.DayReport
import `is`.hi.hbv601g.petraapp.DTOs.DayReportDTO
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class DayReportActivity : AppCompatActivity() {

    private lateinit var mChildName: TextView
    private lateinit var mDateTo: EditText
    private lateinit var mDateFrom: EditText
    private lateinit var mComment: EditText;
    private lateinit var mSubmitReport: Button
    private lateinit var mDayReport: DayReport
    companion object {
        const val TAG: String = "DayReportActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dayreport)

        val bundle = intent.extras
        mChildName = findViewById<EditText>(R.id.child_name)

        val childName = bundle?.getString("childName")

        if (!childName.isNullOrEmpty()) {
            mChildName.text = childName
        }

        val childId = bundle?.getInt("childId")

        mComment = findViewById(R.id.report_comment)

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

            val sleepFrom: LocalDateTime = convertToProperDate(mDateFrom.text.toString())
            val sleepTo: LocalDateTime = convertToProperDate(mDateTo.text.toString())
            println(sleepTo.toString() + " : " + sleepFrom.toString())

            if (sleepFrom > sleepTo) {
                Toast.makeText(this,"'Svefn frá' má ekki byrja á eftir 'svefn til'", Toast.LENGTH_SHORT).show()
            }

            val spinnerIndex = spinner.selectedItemPosition
            if (spinner.selectedItem.toString() === "Matarlyst:") {
                Toast.makeText(this,"Verður að velja matarlyst", Toast.LENGTH_SHORT).show()
            } else {

                val prefs = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                val dcwId = prefs.getString("DCW_ID", null)?.toLong()

                //TODO: Figure out this fucking Appetite!
                mDayReport = DayReport(
                    sleepFrom = sleepFrom,
                    sleepTo = sleepTo,
                    appetite = (spinnerIndex - 1).toString(),
                    comment = mComment.text.toString(),
                    dcwId = dcwId!!,
                    childId = childId!!);

                val nm = NetworkManager.getInstance(this)
                nm.createDayReport(mDayReport, object: NetworkCallback<DayReportDTO>{
                    override fun onSuccess(result: DayReportDTO) {
                        Toast.makeText(this@DayReportActivity,"Gleðilega skýrslu", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DayReportActivity, DcwActivity::class.java)

                        // Pass data to SecondActivity (optional)
//                        intent.putExtra("childName", childName)
//                        intent.putExtra("childId", child.id)
                        ContextCompat.startActivity(this@DayReportActivity, intent, null)
                    }

                    override fun onFailure(errorString: String) {
                        Toast.makeText(this@DayReportActivity,"Úbbsí, eitthvað fór úrskeiðis", Toast.LENGTH_SHORT).show()
                    }
                })
            }


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToProperDate(str: String): LocalDateTime {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val strToDate = timeFormat.parse(str)

        calendar.time = strToDate
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        val date = calendar.time
        val zoneId = ZoneId.systemDefault()
        val instant = date.toInstant()
        return instant.atZone(zoneId).toLocalDateTime()
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