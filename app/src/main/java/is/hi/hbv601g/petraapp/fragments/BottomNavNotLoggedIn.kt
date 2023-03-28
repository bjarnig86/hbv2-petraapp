package `is`.hi.hbv601g.petraapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import `is`.hi.hbv601g.petraapp.Entities.User
import `is`.hi.hbv601g.petraapp.MainActivity
import `is`.hi.hbv601g.petraapp.R

class BottomNavNotLoggedIn : Fragment() {
    private lateinit var mButtonLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.menu_dock_not_logged_in, container, false)
        mButtonLogin = root.findViewById(R.id.login_button)

        mButtonLogin.setOnClickListener {
            if (User.getInstance() == null) {
                (activity as MainActivity?)?.loginWithBrowser()
            } else {
                Toast.makeText(view?.context, "HEHEHEHHE", Toast.LENGTH_LONG).show()
                (activity as MainActivity?)?.logout()
            }
        }
        return root
    }
}