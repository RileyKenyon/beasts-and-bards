package com.example.dungeonsanddragons.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dungeonsanddragons.R
import com.example.dungeonsanddragons.dashboard.data.Game
import com.example.dungeonsanddragons.dashboard.model.GameAdapter
import com.example.dungeonsanddragons.dashboard.model.GameListViewModel
import com.example.dungeonsanddragons.dashboard.model.GameListViewModelFactory
import com.example.dungeonsanddragons.dashboard.model.HeaderAdapter
import com.example.dungeonsanddragons.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val ARG_PARAM1 = "game"

class DashboardFragment : Fragment() {

    companion object {
        const val TAG = "DashboardFragment"
    }

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var usernameTextView: TextView
    private lateinit var user : FirebaseUser
    private val binding get() = _binding!!

    // Setup for passed in newgame
    private var newGame: Game? = null

    // View model setup for recycler view
    private val gameListViewModel by viewModels<GameListViewModel> {
        GameListViewModelFactory(user)
    }
//    private val gameListViewModel: GameListViewModel by activityViewModels()

    // Navigation
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newGame = it.get(ARG_PARAM1) as Game?
        }
//        gameListViewModel.insertGame(newGame)
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
        // TODO: Replace this with the observer pattern
        user = FirebaseAuth.getInstance().currentUser!!
        Log.d(TAG, user.uid)
        val username = user.displayName.toString()
        usernameTextView.text = getString(R.string.welcome_back, username)

        // Game Recycler View
        val headerAdapter = HeaderAdapter()
        val gameAdapter = GameAdapter{game -> adapterOnClick(game)}
        val concatAdapter = ConcatAdapter(headerAdapter, gameAdapter)
        val recyclerView: RecyclerView = binding.gameRecyclerView
        recyclerView.adapter = concatAdapter

        // observe view model
        gameListViewModel.gameLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    gameAdapter.submitList(it as MutableList<Game>)
                    headerAdapter.updateGameCount(it.size)
                }
            }
        }

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
//        navController.navigate(R.id.gameBoard, bundle)
    }

    private fun fabOnClick(){
        // Navigate to gameCreator
        navController.navigate(R.id.createGameFragment)
        // Could also start activity for result
    }
}