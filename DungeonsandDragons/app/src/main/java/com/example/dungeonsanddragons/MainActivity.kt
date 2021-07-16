package com.example.dungeonsanddragons

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dungeonsanddragons.databinding.ActivityMainBinding

private const val TAG = "MainApplication"

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // Set Main view on launch and load D8 gif
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        // Hide action bar
//        supportActionBar?.hide()

        // Check if the NFC adapter triggered the application launch
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages = rawMessages.map { it as NdefMessage }
                val bundle = bundleOf(
                    "rawMessages" to messages
                )
                navController.navigate(R.id.action_startup_to_NFC, bundle)
            }
//            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
//                val currentNdefMessages = rawMessages.map { it as NdefMessage }
//                currentNdefRecords = currentNdefMessages.flatMap { it.records.asList() }
//
//                // Read the payload and convert to a UTF8 string
//                val textView: TextView = findViewById(R.id.nfc_data)
//                textView.text = getCurrentRecordText()
//            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}