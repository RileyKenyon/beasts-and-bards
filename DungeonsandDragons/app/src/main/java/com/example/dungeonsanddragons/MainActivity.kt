package com.example.dungeonsanddragons

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.dungeonsanddragons.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * Main Activity - entry point for the app
 */
class MainActivity : AppCompatActivity() {

    // Navigation instance variables
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Firebase instance variables
    private lateinit var db: FirebaseDatabase

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup navigation and navigation app bar
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Set up Action Bar
        navController = navHostFragment.navController

        // Set of specifies the top level destinations
        appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboardFragment, R.id.nfcFragment),
            binding.navigationDrawer)
        setupActionBar(appBarConfiguration)
        setupNavigationMenu(binding.navigationView)

        // Check for debug mode of the firebase
        // "10.0.2.2" is a special value which allows the Android emulator to
        // connect to "localhost" on the host computer. The port values are
        // defined in the firebase.json file.
        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
            Log.d(TAG, "Using Debug")
        }

        // Initialize Realtime Database
//        db = Firebase.database
//        val messagesRef = db.reference.child(MESSAGES_CHILD)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
//        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
//        if (navigationView != null) {
//            menuInflater.inflate(R.menu.overflow_menu, menu)
//            return true
//        }
//        return retValue
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        // TODO: Find a better way to fix issue with drawer layout extending
        val drawerLayout = findViewById<DrawerLayout>(R.id.navigation_drawer)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Navigate to the destination specified by the menu item
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun setupActionBar(appBarConfig : AppBarConfiguration) {
        // Update action bar automatically when destination changes
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navigationView: NavigationView) {
        navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(menuItem: MenuItem) {
        Log.d(TAG,FirebaseAuth.getInstance().currentUser.toString())
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(TAG,"Signed out successfully")
                navController.navigate(R.id.mainFragment)
            }
    }
}