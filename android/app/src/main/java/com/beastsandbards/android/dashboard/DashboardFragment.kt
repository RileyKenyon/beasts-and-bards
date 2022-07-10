package com.beastsandbards.android.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beastsandbards.android.LoginViewModel
import com.beastsandbards.android.LoginViewModelFactory
import com.beastsandbards.android.R
import com.beastsandbards.android.dashboard.data.Game
import com.beastsandbards.android.dashboard.data.User
import com.beastsandbards.android.dashboard.data.UserSource
import com.beastsandbards.android.dashboard.model.GameAdapter
import com.beastsandbards.android.dashboard.model.GameListViewModel
import com.beastsandbards.android.dashboard.model.GameListViewModelFactory
import com.beastsandbards.android.dashboard.model.HeaderAdapter
import com.beastsandbards.android.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val ARG_PARAM1 = "game"

class DashboardFragment : Fragment() {

    companion object {
        const val TAG = "DashboardFragment"
    }

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var usernameTextView: TextView
    private var user : User? = null
    private val binding get() = _binding!!

    // Setup for passed in newgame
    private var newGame: Game? = null

    // View model setup for recycler view
    private val gameListViewModel by viewModels<GameListViewModel> {
        GameListViewModelFactory(user)
    }

    // Get user information
    private val userViewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory()
    }

    // Navigation
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newGame = it.get(ARG_PARAM1) as Game?
        }
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usernameTextView = binding.welcomeMessage
//        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
//        user = User(firebaseUser.uid.toString(),
//                    firebaseUser.displayName.toString(),
//                    true)
        // Start observing livedata
        userViewModel.userLiveData.observe(viewLifecycleOwner) { liveUser ->
            user = liveUser
//            Log.d(TAG, user!!.uuid)
            val username = user?.name
            usernameTextView.text = getString(R.string.welcome_back, username)
        }



        // Game Recycler View
        val headerAdapter = HeaderAdapter()
        val gameAdapter = GameAdapter{game -> adapterOnClick(game)}
        val concatAdapter = ConcatAdapter(headerAdapter, gameAdapter)
        val recyclerView: RecyclerView = binding.gameRecyclerView
        recyclerView.adapter = concatAdapter

        gameListViewModel.gameLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    gameAdapter.submitList(it as MutableList<Game>)
                    headerAdapter.updateGameCount(it.size)
                }
            }
        }
        // Floating Action Button
        val fab: View = binding.fab
        fab.setOnClickListener{fabOnClick()}
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(game: Game) {
        // Navigate to the gameboard
//        val bundle = bundleOf("game" to game)
        navController.navigate(R.id.questFragment)
    }

    private fun fabOnClick(){
        // Navigate to gameCreator
        navController.navigate(R.id.createGameFragment)
        // Could also start activity for result
    }
}
