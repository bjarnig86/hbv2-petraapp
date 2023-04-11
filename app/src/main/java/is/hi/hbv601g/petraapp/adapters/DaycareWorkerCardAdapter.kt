package `is`.hi.hbv601g.petraapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.MainActivity
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.fragments.ApplicationFragment
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

class DaycareWorkerCardAdapter(private val mDcws: List<DaycareWorker>, private val context: Context, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<DaycareWorkerCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val subTitleView = itemView.findViewById<TextView>(R.id.card_sub_title)

        val expTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_exp_content);

        val phoneTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_phone_content);

        val emailTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_email_content);

        val addressTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_address_content);

        val locationTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_location_content);

        val applyBtn = itemView.findViewById<MaterialButton>(R.id.apply_button)
        val freeSpots = itemView.findViewById<TextView>(R.id.free_spots)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaycareWorkerCardAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val dcwView = inflater.inflate(R.layout.dcwcard, parent, false)

        return ViewHolder(dcwView)
    }

    override fun onBindViewHolder(viewHolder: DaycareWorkerCardAdapter.ViewHolder, position: Int) {
        val dcw: DaycareWorker = mDcws[position]
        val title = viewHolder.titleView
        title.text = dcw.fullName

        val subTitle = viewHolder.subTitleView
        val addressCombined = "${dcw.locationCode}, ${dcw.location}"
        subTitle.text = addressCombined

        val exp = viewHolder.expTitleViewContent
        val expString = "${dcw.experienceInYears} ár"
        exp.text = expString

        val phone = viewHolder.phoneTitleViewContent
        phone.text = dcw.mobile

        val email = viewHolder.emailTitleViewContent
        email.text = dcw.email

        val address = viewHolder.addressTitleViewContent
        address.text = dcw.address

        val location = viewHolder.locationTitleViewContent
        val locationString = "${dcw.locationCode} ${dcw.location}"
        location.text = locationString

        val freeSpots = viewHolder.freeSpots
        freeSpots.text = dcw.freeSpots.toString()

        val applyBtn = viewHolder.applyBtn
        applyBtn.setOnClickListener {
            if (User.getInstance() == null) {
                Toast.makeText(context, "Skráðu þig inn fyrst! :)", Toast.LENGTH_SHORT).show()
            } else if (User.role == "DCW") {
                Toast.makeText(context, "Til að sækja um þarftu að skrá þig inn sem foreldri", Toast.LENGTH_SHORT).show()
            } else {
                val prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                val parentId = prefs.getString("PARENT_ID", "")?.toLong()
                val dcwId = dcw.id.toLong()

                val transaction = fragmentManager.beginTransaction()

                val fragment = parentId?.let { it1 -> ApplicationFragment.newInstance(it1, dcwId) }
                if (fragment != null) {
                    transaction.add(R.id.application_fragment_layout, fragment)
                }
                transaction.addToBackStack(null)

                // Show the background view
                transaction.commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return mDcws.size
    }
}