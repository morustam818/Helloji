package com.rmohd8788.helloji.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.rmohd8788.helloji.MainActivity
import com.rmohd8788.helloji.R
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import kotlinx.android.synthetic.main.fragment_verify_otp.view.*
import java.util.concurrent.TimeUnit

class VerifyOtpFragment : Fragment() {

    private val AUTH_TIMEOUT: Long = 60
    private lateinit var getNumber: String
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var getSmsCode : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view: View = inflater.inflate(R.layout.fragment_verify_otp, container, false)

        //verifying the otp manually
        view.btn_verify.setOnClickListener {
            onButtonVerifyClick()
        }

        //sending the otp again
        view.tv_send_again.setOnClickListener {
          onResendCode()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNumber = arguments?.getString("number").toString()
        verifyPhoneNumber(getNumber)
    }

    private fun verifyPhoneNumber(number: String) {

        pb_verifying_otp.visibility = View.VISIBLE
        timer()
        countDownTimer.start()

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuth: PhoneAuthCredential) {
                if (phoneAuth.smsCode != null) {
                    countDownTimer.cancel()
                    moveToMainActivity()
                    Toast.makeText(activity, "Otp Verified", Toast.LENGTH_SHORT).show()
                    getSmsCode = phoneAuth.smsCode.toString()
                    verifyOtp(getSmsCode)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                pb_verifying_otp.visibility = View.GONE
                countDownTimer.cancel()
                Toast.makeText(activity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                storedVerificationId = verificationId
                resendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                pb_verifying_otp.visibility = View.GONE
                Toast.makeText(activity, "Send code again", Toast.LENGTH_SHORT).show()
            }

        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            AUTH_TIMEOUT,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallbacks
        )
    }

    private fun verifyOtp(otp: String) {
        signInWithPhoneCredential(PhoneAuthProvider.getCredential(storedVerificationId, otp))
    }

    //firebase authentication with phone number
    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                pb_verifying_otp.visibility = View.VISIBLE
                Toast.makeText(activity, "Successfully registered", Toast.LENGTH_SHORT).show()
            } else {
                pb_verifying_otp.visibility = View.GONE
                Toast.makeText(activity, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //resending the otp
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        pb_verifying_otp.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            AUTH_TIMEOUT,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallbacks,
            token
        )
    }

    private fun timer(){
        countDownTimer = object : CountDownTimer(60000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val count = "Resend in "+(millisUntilFinished/1000).toString()
                tv_timer.text = count
            }

            override fun onFinish() {
                tv_timer.visibility = View.GONE
                tv_send_again.visibility = View.VISIBLE
            }
        }
    }

    private fun onResendCode(){
        pb_verifying_otp.visibility = View.VISIBLE
        resendVerificationCode(getNumber, resendToken)
        tv_timer.visibility = View.VISIBLE
        tv_send_again.visibility = View.GONE
        timer()
        countDownTimer.start()
   }

    private fun moveToMainActivity(){
        Intent(activity,MainActivity::class.java).apply {
            startActivity(this)
        }
        activity?.finish()
    }

    private fun onButtonVerifyClick(){
        pb_verifying_otp.visibility = View.INVISIBLE
        tv_timer.visibility = View.GONE
        countDownTimer.cancel()
        tv_send_again.visibility = View.VISIBLE
        val getCodeEnterByUser = et_verification?.editText?.text.toString()
        if (getCodeEnterByUser.isEmpty() || getCodeEnterByUser.length < 6) {
            return
        }else if (getSmsCode != getCodeEnterByUser){
            et_verification.error = "Wrong Otp"
        }
        else{
            et_verification.isErrorEnabled = false
            verifyOtp(getCodeEnterByUser)
        }
    }
}