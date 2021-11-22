package com.example.dungeonsanddragons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.SignUpBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "SignInApplication"

class Signup : AppCompatActivity() {
    private var _binding: SignUpBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    // Firebase configuration
    private val signIn = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in - launch Firebase UI
        // Otherwise proceed to mainActivity
        if (Firebase.auth.currentUser == null) {
            // Sign in with the FirebaseUI
            createSignInIntent()
        } else {
            goToMainActivity()
        }

    }
    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // successfully signed in
            Log.d(TAG,"Sign in successful")
            var user = FirebaseAuth.getInstance().currentUser
        } else {
            // Sign in failed
            Toast.makeText(
                this,
                "There was an error signing in",
                Toast.LENGTH_LONG).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }

    private fun createSignInIntent() {
        // Authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signIn.launch(signInIntent)
    }

    companion object {
        const val TAG = "SignUpActivity"
    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = SignUpBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        textView = binding.textView
//    }
//
//    override fun onDestroyView(){
//        super.onDestroyView()
//        _binding = null
//    }

//
//    private fun delete() {
//        AuthUI.getInstance()
//            .delete(this)
//            .addOnCompleteListener{
//                Log.d(TAG,"Deleted successfully")
//            }
//    }

}