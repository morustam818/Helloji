package com.rmohd8788.helloji.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rmohd8788.helloji.CustomProgressDialog
import com.rmohd8788.helloji.R

class GoogleAuthFragment : Fragment() {

    private lateinit var customProgressDialog: CustomProgressDialog

    companion object{
        const val RC_SIGN_IN = 1
        const val TAG = "GoogleAccount"
    }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGso: GoogleSignInOptions
    private lateinit var googleClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_google_auth, container, false)

        customProgressDialog = CustomProgressDialog(requireActivity())
        signInWithGoogle()
        customProgressDialog.startProgress()

        return view
    }

    private fun signInWithGoogle() {
        mGso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(requireActivity(), mGso)

        startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        Intent(googleClient.signInIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task : Task<GoogleSignInAccount>? = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task!!)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            customProgressDialog.stopProgress()
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!.idToken.toString())
            Toast.makeText(activity, "Successfully Account created", Toast.LENGTH_SHORT).show()
            moveToFragmentDependOnResult(PhoneNumberAuth())
        } catch (e: ApiException) {

            customProgressDialog.stopProgress()
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            moveToFragmentDependOnResult(SignInFragment())
        }
    }

    private fun firebaseAuthWithGoogle(idToken : String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Log.i(TAG, "firebaseAuthWithGoogle: ")
            }else{
                Toast.makeText(context,"Authentication Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToFragmentDependOnResult(fragment: Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.mainFragmentContainer,fragment)?.commit()
    }
}