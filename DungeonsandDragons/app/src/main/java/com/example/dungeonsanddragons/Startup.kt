package com.example.dungeonsanddragons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.dungeonsanddragons.databinding.StartupBinding

private const val TAG = "Startup"

class Startup : Fragment() {

    // Initialization and binding
    private var _binding: StartupBinding? = null
    private lateinit var imageView: ImageView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = StartupBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StartupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set Main view on launch and load D8 gif
        Glide.with(this).load(R.drawable.title).into(binding.titleImage)
        imageView = binding.titleImage

        // Login button bindings
        binding.loginButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startup_to_login)
        )
        // Signup button bindings
        binding.signUpButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startup_to_sign_up)
        )
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

}