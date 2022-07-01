package com.example.dungeonsanddragons

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dungeonsanddragons.databinding.FragmentMainBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

/**
 * Main Fragment to handle authentication - monitor the live userdata
 * If user signs out, re-launch the Firebase UI
 */

class MainFragment : Fragment() {
    companion object {
        const val TAG = "MainFragment"
    }

    // Get a reference to the ViewModel scoped to this fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var _binding: FragmentMainBinding
    private val binding get() = _binding

    // Firebase configuration
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Monitor user authentication
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        viewModel.firebaseUserData.observe(viewLifecycleOwner, Observer { user ->
            val navController = findNavController()
            if (user != null) {
                Log.d(TAG, user.displayName.toString())
                navController.navigate(R.id.action_mainFragment_to_dashboardFragment)
            } else {
                Log.d(TAG, "null user")
                launchSignInFlow()
            }
        })
    }

    private fun launchSignInFlow() {
        // Authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
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
        if (result.resultCode == Activity.RESULT_OK) {
            // successfully signed in
            Log.d(MainFragment.TAG, "Sign in successful")
        } else {
            val response = result.idpResponse
            if (response == null) {
                Log.w(MainFragment.TAG, "Sign in canceled")
            } else {
                Log.w(MainFragment.TAG, "Sign in error", response.error)
            }
        }
    }
}
