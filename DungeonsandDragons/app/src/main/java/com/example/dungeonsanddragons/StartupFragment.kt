package com.example.dungeonsanddragons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.FragmentStartupBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartupFragment : Fragment() {
    private var _binding: FragmentStartupBinding? = null
    private val binding get() = _binding!!

    // Firebase configuration
//    private val signInLauncher = registerForActivityResult(
//        FirebaseAuthUIActivityResultContract()
//    ) { res ->
//        this.onSignInResult(res)
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _binding = FragmentStartupBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//    }

//    public override fun onStart() {
//        super.onStart()
//
//        // Check if user is signed in - launch Firebase UI, otherwise proceed to mainActivity
//        if (Firebase.auth.currentUser == null) {
//            createSignInIntent()
//        } else {
//            goToMainActivity()
//        }
//    }

//    private fun goToMainActivity() {
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }

//    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
//        val response = result.idpResponse
//        if (result.resultCode == RESULT_OK) {
//            // successfully signed in
//            Log.d(TAG,"Sign in successful")
//            var user = FirebaseAuth.getInstance().currentUser
//            goToMainActivity()
//        } else {
//            // Sign in failed
//            Toast.makeText(
//                this,
//                "There was an error signing in",
//                Toast.LENGTH_LONG).show()
//
//            val response = result.idpResponse
//            if (response == null) {
//                Log.w(TAG, "Sign in canceled")
//            } else {
//                Log.w(TAG, "Sign in error", response.error)
//            }
//        }
//    }
//
//    private fun createSignInIntent() {
//        // Authentication providers
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
//        )
//
//        // Format UI for authentication
//        val customLayout = AuthMethodPickerLayout
//            .Builder(R.layout.fragment_startup)
//            .setGoogleButtonId(R.id.sign_in_google)
//            .setEmailButtonId(R.id.sign_in_email)
//            .build()
//
//        // Create and launch sign-in intent
//        val signInIntent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAuthMethodPickerLayout(customLayout)
//            .setAvailableProviders(providers)
//            .setTheme(R.style.Theme_DungeonsAndDragons)
//            .setLogo(R.drawable.dice)
//            .build()
//        signInLauncher.launch(signInIntent)
//    }
//
//    private fun signOut() {
//        AuthUI.getInstance()
//            .signOut(this)
//            .addOnCompleteListener{
//                Log.d(StartupFragment.TAG,"Signed out successfully")
//            }
//    }

    companion object {
        const val TAG = "StartupActivity"
    }

}