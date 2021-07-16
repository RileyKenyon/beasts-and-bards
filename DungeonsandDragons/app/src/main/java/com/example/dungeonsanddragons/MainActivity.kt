package com.example.dungeonsanddragons

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dungeonsanddragons.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar

private const val TAG = "MainApplication"

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // Set Main view on launch and load D8 gif
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setSupportActionBar(findViewById<MaterialToolbar>(R.id.topAppBar))
        // Auto Action bar with navigateUp()
//        setupActionBarWithNavController(navController,appBarConfiguration)

        // Alternate toolbar setup
        findViewById<MaterialToolbar>(R.id.topAppBar).setupWithNavController(navController,appBarConfiguration)

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
//        return navController.navigateUp() || super.onSupportNavigateUp()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}