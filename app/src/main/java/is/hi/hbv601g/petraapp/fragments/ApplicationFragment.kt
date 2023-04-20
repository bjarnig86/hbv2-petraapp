package `is`.hi.hbv601g.petraapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import `is`.hi.hbv601g.petraapp.Entities.ApplicationDTO
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

private const val ARG_PARAM1 = "parent"
private const val ARG_PARAM2 = "dcw"
private const val ARG_PARAM3 = "dcwName"
private const val TAG = "ApplicationFragment"

class ApplicationFragment : Fragment() {

    private var parentId: Long? = null
    private var dcwId: Long? = null
    private var dcwName: String? = null

    private var childId: Long? = null

    private lateinit var childListView: ListView
    private lateinit var selectChildButton: Button
    private lateinit var cancelButton: Button
    private lateinit var greeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parentId = it.getLong(ARG_PARAM1)
            dcwId = it.getLong(ARG_PARAM2)
            dcwName = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_application, container, false)

        childListView = view.findViewById(R.id.child_auto_complete_text_view)
        selectChildButton = view.findViewById(R.id.select_child_button)
        cancelButton = view.findViewById(R.id.select_child_cancel_button)
        greeting = view.findViewById(R.id.application_text_view)
        greeting.text = "Veldu barn fyrir umsókn hjá \n$dcwName"

        val nm = NetworkManager.getInstance(requireContext())
        parentId?.let { nm.getUnregisteredChildrenByParent(it, object : NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {

                Log.d(TAG, "onSuccess: $result")
                val childAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    result
                )
                childListView.adapter = childAdapter
                childListView.choiceMode = ListView.CHOICE_MODE_SINGLE
            }

            override fun onFailure(errorString: String) {
                Log.e(TAG, "onFailure: $errorString")
            }

        }) }

        childListView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, pos, id ->
            val child: Child = childListView.getItemAtPosition(pos) as Child
            childId = child.id.toLong()
        }

        selectChildButton.setOnClickListener {
            if (childId != null && parentId != null && dcwId != null) {
                nm.applyToDCW(childId!!, parentId!!, dcwId!!, object: NetworkCallback<ApplicationDTO> {
                    override fun onSuccess(result: ApplicationDTO) {
                        Log.d(TAG, "onSuccess: Application successful!")
                        // Get the parent FragmentManager and remove this fragment
                        parentFragmentManager.beginTransaction().remove(this@ApplicationFragment).commit()
                    }

                    override fun onFailure(errorString: String) {
                        Log.e(TAG, "onFailure: Application failed... error: $errorString")
                    }

                })
            }
        }

        cancelButton.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@ApplicationFragment).commit()
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: Long, dcw: Long, dcwName: String) =
            ApplicationFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, parent)
                    putLong(ARG_PARAM2, dcw)
                    putString(ARG_PARAM3, dcwName)
                }
            }
    }
}
