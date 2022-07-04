package com.beastsandbards.android

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.beastsandbards.android.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

    // view variables
    private lateinit var binding : ActivityMainBinding
    companion object {
        private const val TAG = "MainActivity"
        const val host = "10.0.2.2"
        const val databasePort = 9000
        const val authPort = 9099
        const val storagePort = 9199
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup navigation and navigation app bar
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Set up Action Bar
        navController = navHostFragment.navController

        // Set of specifies the top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.dashboardFragment, R.id.nfcFragment, R.id.questFragment),
            binding.navigationDrawer)
        setupActionBar(appBarConfiguration)
        setupNavigationMenu(binding.navigationView)

        // Watch for navigation destination
        bottomNavBarVisibility(navController)

        // Setup action bar
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.character_sheet -> {
                    Log.d(MainActivity.TAG,"Character sheet")
                    true
                }
                R.id.inventory -> {
                    Log.d(MainActivity.TAG,"Inventory")
                    true
                }
                R.id.monster_manual -> {
                    Log.d(MainActivity.TAG,"Monster Manual")
                    true
                }
                R.id.map -> {
                    Log.d(MainActivity.TAG,"Map")
                    true
                }
                else -> false
            }
        }

        // Check for debug mode of the firebase
        // "10.0.2.2" is a special value which allows the Android emulator to
        // connect to "localhost" on the host computer. The port values are
        // defined in the firebase.json file.

        if (BuildConfig.FIREBASE_DEBUG) {
            Firebase.database.useEmulator(host, databasePort)
            Firebase.auth.useEmulator(host, authPort)
            Firebase.storage.useEmulator(host, storagePort)
            Log.d(TAG, "Using Firebase Debugging")
        }

        // Initialize Realtime Database
//        db = Firebase.database
//        val messagesRef = db.reference.child(MESSAGES_CHILD)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        Log.d(TAG, FirebaseAuth.getInstance().currentUser?.displayName.toString())
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(TAG,"Signed out successfully")
                navController.navigate(R.id.mainFragment)
            }
    }

    // Add listener to hide the bottom nav bar
    private fun bottomNavBarVisibility(navController : NavController) {
//        val v = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment,
                R.id.nfcFragment,
                R.id.createGameFragment,
                R.id.dashboardFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}
