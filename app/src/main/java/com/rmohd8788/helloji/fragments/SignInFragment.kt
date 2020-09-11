package com.rmohd8788.helloji.fragments


import android.content.Intent
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
import com.rmohd8788.helloji.MainActivity
import com.rmohd8788.helloji.PreferenceHelper
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SignInFragment : Fragment() {

    companion object{
        const val IS_CHECKED = "remember"
        const val EMAIL_ID = "email_id"
        const val PASSWORD = "password"
    }

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mEmail : String
    private lateinit var mPassword : String
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var customProgressDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_sign_in, container, false)

        preferenceHelper = PreferenceHelper(requireContext())
        customProgressDialog = CustomProgressDialog(requireActivity())

        //for google sign in
        view.btn_login_google.setOnClickListener {
            replaceFragment(GoogleAuthFragment())
        }

        //for facebook
        view.btn_login_fb.setOnClickListener {
            customProgressDialog.startProgress()
            addToBackStack(FacebookAuthFragment())
        }

        //for sign up fragment
        view.tv_login_sign_up.setOnClickListener {
            replaceFragment(SignUpFragment())
        }

        view.tv_forgot_p.setOnClickListener {
            addToBackStack(ForgotPasswordFragment())
        }


        //login
        view.btn_login.setOnClickListener {
            handlingLoginBtn()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailTextWatcher()
        passTextWatcher()
        wasChecked()
    }

    //login button
    private fun handlingLoginBtn() {
        if (rd_remember.isChecked) {
            //inserting data into shared pref
            preferenceHelper.putBoolean(IS_CHECKED, true)
            preferenceHelper.putString(EMAIL_ID, mEmail)
            preferenceHelper.putString(PASSWORD, mPassword)
        } else {
            preferenceHelper.clear()
        }

        signInWithEmailAndPass(mEmail,mPassword)
    }

    private fun signInWithEmailAndPass(email :String,password : String) {
        customProgressDialog.startProgress()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
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
            .replace(R.id.mainFragmentContainer, fragment)
            .addToBackStack("back stack")
            .commit()
    }

    //replacing the fragment
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_frm_right, R.anim.slideout_frm_left)
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }

    //check If Radio Was Checked On Previous Login, if yes move to main activity
    private fun wasChecked(){
        customProgressDialog.startProgress()
        if (preferenceHelper.getBoolean(IS_CHECKED)){
            //retrieving the data from shared helper class
            val getEmail = preferenceHelper.getString(EMAIL_ID)
            val getPass = preferenceHelper.getString(PASSWORD)
            signInWithEmailAndPass(getEmail!!,getPass!!)
            Toast.makeText(requireContext(),"Automatically logged in",Toast.LENGTH_SHORT).show()
        }
        customProgressDialog.stopProgress()
    }


    //text watcher for email
    private fun emailTextWatcher() {
        et_login_email.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    //text watcher for email
    private fun passTextWatcher() {
        et_login_pass.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    //make sure user entering the correct email address
    private fun validateEmail(): Boolean {
        mEmail = et_login_email.editText?.text.toString()
        return if (mEmail.trim().isEmpty()) {
            et_login_email.error = "Field cannot be empty"
            btn_login.isEnabled = false
            false
        } else if (!mEmail.matches(emailPattern.toRegex())) {
            et_login_email.error = "Invalid Email"
            btn_login.isEnabled = false
            false
        } else {
            et_login_email.isErrorEnabled = false
            true
        }
    }

    //password must be very strong
    private fun validatePassword(): Boolean {
        mPassword = et_login_pass.editText?.text.toString()
        return when {
            mPassword.trim().isEmpty() -> {
                et_login_pass.error = "Field can't be empty"
                btn_login.isEnabled = false
                buttonSignInTextColor(resources.getColor(R.color.colorLightGrey))
                false
            }
            mPassword.length < 7 -> {
                btn_login.isEnabled = false
                et_login_pass.helperText = "Password must be at least 8 characters"
                buttonSignInTextColor(resources.getColor(R.color.colorLightGrey))
                false
            }
            else -> {
                buttonSignInTextColor(resources.getColor(R.color.colorWhite))
                et_login_pass.isErrorEnabled = false
                et_login_pass.isHelperTextEnabled = false
                btn_login.isEnabled = true
                true
            }
        }
    }

    //to avoid duplication
    private fun buttonSignInTextColor(color: Int) {
        btn_login.setTextColor(color)
    }

    private fun moveToMainActivity(){
        Intent(context,MainActivity::class.java).apply {
            startActivity(this)
        }
    }

}
//LoginManager.getInstance().logOut()