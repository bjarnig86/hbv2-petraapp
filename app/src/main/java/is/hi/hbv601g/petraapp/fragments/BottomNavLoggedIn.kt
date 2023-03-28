package `is`.hi.hbv601g.petraapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.petraapp.DcwActivity
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.MainActivity
import `is`.hi.hbv601g.petraapp.ParentActivity
import `is`.hi.hbv601g.petraapp.R

class BottomNavLoggedIn : Fragment(R.layout.menu_dock_logged_in) {
    private lateinit var mNavBar: View;
    private lateinit var mPetraBtn: ImageButton
    private lateinit var mHomeBtn: ImageButton
    lateinit var res: Drawable;

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.menu_dock_logged_in, container, false)
        mNavBar = root.findViewById(R.id.custom_action_bar)
        mPetraBtn = root.findViewById(R.id.action_bar_petra)
        mHomeBtn = root.findViewById(R.id.action_bar_home)

        // Disabling and handling which acvitity user is on
        if (activity is ParentActivity) {
            val drawableResId = R.drawable.kerra_selected;
            val drawable = resources.getDrawable(drawableResId, null)
            mPetraBtn.setImageDrawable(drawable);
            mPetraBtn.isEnabled = false;
        }

        if (activity is MainActivity) {
            val drawableResId = R.drawable.house_solid_selected;
            val drawable = resources.getDrawable(drawableResId, null)
            mHomeBtn.setImageDrawable(drawable);
            mHomeBtn.isEnabled = false;
        }

        // On click listeners to take user to selected activity
        mPetraBtn.setOnClickListener {
            if (User.role == "Parent") {
                val intent = Intent(requireContext(), ParentActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), DcwActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        mHomeBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            // Flag to go to the previous MainActiivity instead of creating a new one, creates a new
            // one only if no previous existed
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        return root
    }
}