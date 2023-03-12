package `is`.hi.hbv601g.petraapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.R

class DaycareWorkerCardAdapter(private val mDcws: List<DaycareWorker>) : RecyclerView.Adapter<DaycareWorkerCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.card_title)
        val subTitleView = itemView.findViewById<TextView>(R.id.card_sub_title)

        val expTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_exp_content);

        val phoneTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_phone_content);

        val emailTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_email_content);

        val addressTitleViewContent = itemView.findViewById<TextView>(R.id.card_list_address_content);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaycareWorkerCardAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val dcwView = inflater.inflate(R.layout.dcwcard, parent, false)
        return ViewHolder(dcwView)
    }

    override fun onBindViewHolder(viewHolder: DaycareWorkerCardAdapter.ViewHolder, position: Int) {
        val dcw: DaycareWorker = mDcws.get(position)
        val title = viewHolder.titleView
        title.text = dcw.fullName

        val subTitle = viewHolder.subTitleView
        val addressCombined = "${dcw.locationCode}, ${dcw.location}"
        subTitle.text = addressCombined

        val exp = viewHolder.expTitleViewContent
        val expString = "${dcw.experienceInYears.toString()} ár"
        exp.text = expString

        val phone = viewHolder.phoneTitleViewContent
        phone.text = dcw.mobile

        val email = viewHolder.emailTitleViewContent
        email.text = dcw.email

        val address = viewHolder.addressTitleViewContent
        address.text = dcw.address
    }

    override fun getItemCount(): Int {
        return mDcws.size
    }
}