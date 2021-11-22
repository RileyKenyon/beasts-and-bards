package com.example.dungeonsanddragons

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private const val TAG = "MainApplication"

class MainActivity : AppCompatActivity() {

    // Navigation instance variables
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

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

        // Check for debug mode of the firebase
        // "10.0.2.2" is a special value which allows the Android emulator to
        // connect to "localhost" on the host computer. The port values are
        // defined in the firebase.json file.
        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }

        // Check if user is signed in
        val user = Firebase.auth.currentUser
        if (user == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, Signup::class.java))
//            navController.navigate(R.id.startup)
//            finish()
//            return
        }

        // Initialize Realtime Database
//        db = Firebase.database
//        val messagesRef = db.reference.child(MESSAGES_CHILD)

        // Check if the NFC adapter triggered the application launch
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
//            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
//                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
//                val messages = rawMessages.map { it as NdefMessage }
//                val bundle = bundleOf(
//                    "rawMessages" to messages,
//                    "id" to tag?.id
//                )
//                navController.navigate(R.id.action_startup_to_NFC, bundle)
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(Signup.TAG,"Signed out successfully")
            }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}