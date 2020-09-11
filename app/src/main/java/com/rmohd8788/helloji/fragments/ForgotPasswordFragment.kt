package com.rmohd8788.helloji.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*

class ForgotPasswordFragment : Fragment() {

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"
    private lateinit var resetEmail: String
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        mAuth = FirebaseAuth.getInstance()
        view.btn_reset_email.setOnClickListener {
            handlingResetButtonClick()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatcher()
    }

    private fun addTextWatcher() {
        et_reset_email.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun handlingResetButtonClick() {
        if (!validateEmail()) {
            return
        }
        TransitionManager.beginDelayedTransition(linear)
        pb_forgot.visibility = View.VISIBLE
        TransitionManager.beginDelayedTransition(linear)
        sendEmailIcon.visibility = View.VISIBLE
        tv_send_status.visibility = View.GONE
        mAuth.sendPasswordResetEmail(resetEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccessEmailSend()
                } else {
                    onFailed(task)
                }
            }
    }

    private fun validateEmail(): Boolean {
        resetEmail = et_reset_email.editText!!.text.toString()
        return if (resetEmail.trim().isEmpty()) {
            et_reset_email.error = "Field cannot be empty"
            btn_reset_email.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorLightGrey))
            btn_reset_email.isEnabled = false
            false
        } else if (!resetEmail.matches(emailPattern.toRegex())) {
            et_reset_email.error = "Invalid Email"
            btn_reset_email.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorLightGrey))
            btn_reset_email.isEnabled = false
            false
        } else {
            btn_reset_email.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorWhite))
            btn_reset_email.isEnabled = true
            et_reset_email.isErrorEnabled = false
            true
        }
    }


    private fun onFailed(task: Task<Void>) {
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        pb_forgot.visibility = View.GONE
        sendEmailIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.mail_red))
        tv_send_status.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorRed))
        tv_send_status.text = task.exception!!.message
        TransitionManager.beginDelayedTransition(linear)
        tv_send_status.visibility = View.VISIBLE
        btn_reset_email.isEnabled = true
        btn_reset_email.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorWhite))
    }

    private fun onSuccessEmailSend() {
        et_reset_email.editText!!.text = null
        et_reset_email.editText!!.hint = "Email"
        et_reset_email.isErrorEnabled = false
        pb_forgot.visibility = View.GONE
        sendEmailIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.success_mail))
        tv_send_status.setTextColor(resources.getColor(R.color.greenColor))
        tv_send_status.text =
            resources.getText(R.string.recovery_email_sent_successfully_check_your_inbox)
        TransitionManager.beginDelayedTransition(linear)
        tv_send_status.visibility = View.VISIBLE
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        btn_reset_email.isEnabled = false
        btn_reset_email.setTextColor(resources.getColor(R.color.colorLightGrey))
    }

    //TODO do not forget to clean code after successful implementation, like - setting the text color

}