package com.example.dungeonsanddragons

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.SignUpBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "SignInApplication"

abstract class Signup : AppCompatActivity() {
    private var _binding: SignUpBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    // Firebase configuration
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().useEmulator('10.0.2.2', 9099);
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
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // successfully signed in
            var user = FirebaseAuth.getInstance().currentUser
        } else {
            // Sign in failed
        }
    }
     private fun signOut() {
         AuthUI.getInstance()
             .signOut(this)
             .addOnCompleteListener{
                 Log.d(TAG,"Signed out successfully")
             }
     }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener{
                Log.d(TAG,"Deleted successfully")
            }
    }

}