package com.example.dungeonsanddragons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.FragmentDashboardBinding
import com.firebase.ui.auth.AuthUI

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = binding.welcomeMessage
        // TODO: Replace this with the observer pattern
        val user = AuthUI.getInstance().auth.currentUser
        val username = user?.email.toString()
        textView.text = getString(R.string.welcome_back, username)
//        binding.signoutButton.setOnClickListener(
//            Navigation.createNavigateOnClickListener(R.id.NFC)
//        )
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}