package com.rmohd8788.helloji.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rmohd8788.helloji.CustomProgressDialog
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_welcome_screen.view.*

class WelcomeScreen : Fragment() {

    private lateinit var customProgressDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_welcome_screen, container, false)
        customProgressDialog = CustomProgressDialog(requireActivity())

        view.btn_wc_sign_up.setOnClickListener {
            addFragment(SignUpFragment())
        }

        view.tv_wc_login.setOnClickListener {
            addFragment(SignInFragment())
        }

        view.btn_wc_fb.setOnClickListener {
            customProgressDialog.startProgress()
            addToBackStack(FacebookAuthFragment())
        }

        view.btn_wc_google.setOnClickListener {
            addFragment(GoogleAuthFragment())
        }

        return view
    }

    //currently facebook isn't working may be the server has been shutdown
    private fun addToBackStack(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_frm_right, R.anim.slideout_frm_left)
            .add(R.id.mainFragmentContainer, fragment)
            .addToBackStack("back stack")
            .commit()
    }

    private fun addFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_frm_right, R.anim.slideout_frm_left)
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }
}