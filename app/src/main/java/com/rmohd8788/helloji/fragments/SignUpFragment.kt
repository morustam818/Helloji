package com.rmohd8788.helloji.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rmohd8788.helloji.CustomProgressDialog
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment() {


    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"
    private val passwordPattern =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    private lateinit var customProgressDialog : CustomProgressDialog
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)

        customProgressDialog = CustomProgressDialog(requireActivity())

        view.btn_sign_up.setOnClickListener {
            if (view.rd_accept.isChecked) {
                signUpWithEmailAndPass(mEmail,mPassword)
            }else{
                Toast.makeText(requireContext(), "Please accept privacy and policy", Toast.LENGTH_SHORT).show()
            }
        }

        view.btn_sign_up_google.setOnClickListener {
            addToBackStack(GoogleAuthFragment())
        }

        view.tv_sign_up_login.setOnClickListener {
            replaceFragment(SignInFragment())
        }

        view.btn_sign_up_fb.setOnClickListener {
            customProgressDialog.startProgress()
            addToBackStack(FacebookAuthFragment())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailTextWatcher()
        passTextWatcher()
    }

    private fun signUpWithEmailAndPass(email : String, password : String) {
        customProgressDialog.startProgress()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    customProgressDialog.stopProgress()
                    replaceFragment(PhoneNumberAuth())
                }
            }.addOnFailureListener {
                customProgressDialog.stopProgress()
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToBackStack(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_frm_right, R.anim.slideout_frm_left)
            .add(R.id.mainFragmentContainer, fragment)
            .addToBackStack("back stack")
            .commit()
    }


    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_frm_left, R.anim.slideout_frm_right)
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }

    //text watcher for email
    private fun emailTextWatcher() {
        et_sign_up_email.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    //text watcher for email
    private fun passTextWatcher() {
        et_sign_up_pass.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
            override fun afterTextChanged(s: Editable?) {}

        })
    }

    //make sure user entering the correct email address
    private fun validateEmail(): Boolean {
        mEmail = et_sign_up_email.editText?.text.toString()
        return if (mEmail.trim().isEmpty()) {
            et_sign_up_email.error = "Field cannot be empty"
            btn_sign_up.isEnabled = false
            false
        } else if (!mEmail.matches(emailPattern.toRegex())) {
            et_sign_up_email.error = "Invalid Email"
            btn_sign_up.isEnabled = false
            false
        } else {
            et_sign_up_email.isErrorEnabled = false
            true
        }
    }

    //password must be very strong
    private fun validatePassword(): Boolean {
        mPassword = et_sign_up_pass.editText?.text.toString()
        return if (mPassword.trim().isEmpty()) {
            et_sign_up_pass.error = "Field can't be empty"
            btn_sign_up.isEnabled = false
            buttonSignUpTextColor(resources.getColor(R.color.colorLightGrey))
            false
        } else if (mPassword.length < 7 || !mPassword.matches(passwordPattern.toRegex())) {
            et_sign_up_pass.helperText =
                "Password must have at least 8 characters and contains at least 1 : " +
                        "uppercase letters, lowercase letters, symbols and numbers."
            btn_sign_up.isEnabled = false
            buttonSignUpTextColor(resources.getColor(R.color.colorLightGrey))
            false
        } else {
            et_sign_up_pass.isHelperTextEnabled = false
            et_sign_up_pass.isErrorEnabled = false
            btn_sign_up.isEnabled = true
            buttonSignUpTextColor(resources.getColor(R.color.colorWhite))
            true
        }
    }

    //to avoid duplication
    private fun buttonSignUpTextColor(color: Int) {
        btn_sign_up.setTextColor(color)
    }
}