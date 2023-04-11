package `is`.hi.hbv601g.petraapp.fragments

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
import androidx.core.view.get
import `is`.hi.hbv601g.petraapp.Entities.Child
import `is`.hi.hbv601g.petraapp.R
import `is`.hi.hbv601g.petraapp.networking.NetworkCallback
import `is`.hi.hbv601g.petraapp.networking.NetworkManager

private const val ARG_PARAM1 = "parent"
private const val ARG_PARAM2 = "dcw"

class ApplicationFragment : Fragment() {

    private var parentId: Long? = null
    private var dcwId: Long? = null

    private lateinit var childListView: ListView
    private lateinit var selectChildButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parentId = it.getLong(ARG_PARAM1)
            dcwId = it.getLong(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_application, container, false)

        childListView = view.findViewById(R.id.child_auto_complete_text_view)
        selectChildButton = view.findViewById(R.id.select_child_button)

        val nm = NetworkManager.getInstance(requireContext())
        parentId?.let { nm.getChildrenByParent(it, object : NetworkCallback<List<Child>> {
            override fun onSuccess(result: List<Child>) {
                Log.d("ApplicationFragment", "onSuccess: $result")
                val childAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    result
                )
                childListView.adapter = childAdapter
                

            }

            override fun onFailure(errorString: String) {
                Log.e("ApplicationFragment", "onFailure: $errorString")
            }

        }) }
        selectChildButton.setOnClickListener {
            // TODO: handle button click and send request with selected child's ID
        }

        childListView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, pos, id ->
            val child: Child = childListView.getItemAtPosition(pos) as Child
            Log.d("ApplicationFragment", "onCreateView: ${child.id}")
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: Long, dcw: Long) =
            ApplicationFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, parent)
                    putLong(ARG_PARAM2, dcw)
                }
            }
    }
}
