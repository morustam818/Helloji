package com.rmohd8788.helloji.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_phone_number_auth.*
import kotlinx.android.synthetic.main.fragment_phone_number_auth.view.*


class PhoneNumberAuth : Fragment() {

    private lateinit var number: String

    private val verifyOtpFragment = VerifyOtpFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_phone_number_auth, container, false)

        view.btn_send_otp.setOnClickListener {
            moveToVerifyOtpFragment(view)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addWatcher()
    }

    private fun moveToVerifyOtpFragment(view: View) {
        view.pb_send_otp.visibility = View.VISIBLE
        val args = Bundle()
        args.putString("number", "+91$number")
        verifyOtpFragment.arguments = args
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_frm_right, R.anim.slideout_frm_left)
            ?.replace(R.id.mainFragmentContainer, verifyOtpFragment)
            ?.addToBackStack("backStack")
            ?.commit()
    }

    //validating the phone number enter by user
    private fun validPhoneNumber(): Boolean {
        number = et_phone.editText?.text.toString()

        return when {
            number.trim().isEmpty() -> {
                et_phone.helperText = "Field can't be empty"
                btn_send_otp.isEnabled = false
                buttonTextColor(resources.getColor(R.color.colorLightGrey))
                false
            }
            number.length != 10 -> {
                btn_send_otp.isEnabled = false
                et_phone.isHelperTextEnabled = false
                buttonTextColor(resources.getColor(R.color.colorLightGrey))
                false
            }
            else -> {
                btn_send_otp.isEnabled = true
                et_phone.isHelperTextEnabled = false
                buttonTextColor(resources.getColor(R.color.colorWhite))
                true
            }
        }
    }

    //make sure number is only 10 digits
    private fun addWatcher() {
        et_phone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validPhoneNumber()
            }
            override fun afterTextChanged(s: Editable?) {}

        })
    }

    //setting the send otp button color
    private fun buttonTextColor(color: Int) {
        btn_send_otp.setTextColor(color)
    }


}