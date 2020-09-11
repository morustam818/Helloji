package com.rmohd8788.helloji.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.rmohd8788.helloji.CustomProgressDialog
import com.rmohd8788.helloji.R

class FacebookAuthFragment : Fragment() {

    companion object {
        const val RC_SIGN_IN = 1
        const val TAG = "FacebookAuth"
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCallback: CallbackManager
    private lateinit var accessTokenTracker: AccessTokenTracker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_facebook_auth, container, false)
        accessTokenTracker()
        signInWithFacebook()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    private fun signInWithFacebook() {
        mCallback = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email","pubic_profile"))
        LoginManager.getInstance().registerCallback(mCallback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthWithFacebook(result!!.accessToken)
                moveToPhoneAuth()
                Log.i(TAG, "onSuccess: ")
            }

            override fun onCancel() {
                Log.i(TAG, "onFailed: ")
                onFailed()
            }

            override fun onError(error: FacebookException?) {
                Log.i(TAG, "onError: ")
                onFailed()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallback.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if(data == null){
            onFailed()
        }
    }

    //adding to firebase
    private fun firebaseAuthWithFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Firebase Authentication successful", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun accessTokenTracker(){2
        accessTokenTracker = object : AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if (currentAccessToken == null){
                    mAuth.signOut()
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.mainFragmentContainer,SignInFragment())?.commit()
                }
            }

        }
    }

    private fun onFailed(){
        Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.mainFragmentContainer,this@FacebookAuthFragment)?.commit()
    }

    private fun moveToPhoneAuth(){
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_frm_right,R.anim.slideout_frm_left)
            ?.replace(R.id.mainFragmentContainer,PhoneNumberAuth())
            ?.commit()

    }

}