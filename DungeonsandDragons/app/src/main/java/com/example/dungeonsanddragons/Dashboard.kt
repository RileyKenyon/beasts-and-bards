package com.example.dungeonsanddragons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dungeonsanddragons.databinding.DashboardBinding
import com.firebase.ui.auth.AuthUI

class Dashboard : Fragment() {
    private var _binding: DashboardBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = binding.welcomeMessage
        val user = AuthUI.getInstance().auth.currentUser
        val username = user?.email.toString()
        textView.text = getString(R.string.welcome_back,username)

        // Add callback for button listener

        binding.signoutButton.setOnClickListener(
////            AuthUI.getInstance().signOut(user)
//            startActivity(Intent(this, Startup::class.java))
//            Navigation.createNavigateOnClickListener(R.id.action_dashboard_to_startup,
//                bundleOf ("signout" to true)
//            )
//        )
    }
//    override fun onClick(view: View): View.OnClickListener? {
//        AuthUI.getInstance().signOut(requireContext())
//    }


    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}