package com.example.dungeonsanddragons

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.dungeonsanddragons.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    // Get reference to ViewModel scoped for this Fragment
    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var navController : NavController

    // Firebase configuration
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private var _binding: FragmentLoginBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.authButton.setOnClickListener {launchSignInFlow()}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun launchSignInFlow() {
        // Authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Format UI for authentication
        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.fragment_login)
            .setGoogleButtonId(R.id.sign_in_google)
            .setEmailButtonId(R.id.sign_in_email)
            .build()

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAuthMethodPickerLayout(customLayout)
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_DungeonsAndDragons)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            // successfully signed in
            Log.d(StartupFragment.TAG,"Sign in successful")
            var user = FirebaseAuth.getInstance().currentUser
//            goToMainActivity()
        } else {
            val response = result.idpResponse
            if (response == null) {
                Log.w(StartupFragment.TAG, "Sign in canceled")
            } else {
                Log.w(StartupFragment.TAG, "Sign in error", response.error)
            }
        }
    }
}