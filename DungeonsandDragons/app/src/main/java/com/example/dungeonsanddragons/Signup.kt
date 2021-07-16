package com.example.dungeonsanddragons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.SignUpBinding

class Signup : Fragment() {
    private var _binding: SignUpBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = binding.textView
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}