package com.example.dungeonsanddragons.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dungeonsanddragons.R
import com.example.dungeonsanddragons.dashboard.data.Player
import com.example.dungeonsanddragons.dashboard.model.PlayerAdapter
import com.example.dungeonsanddragons.dashboard.model.PlayerListViewModel
import com.example.dungeonsanddragons.dashboard.model.PlayerListViewModelFactory
import com.example.dungeonsanddragons.databinding.FragmentCreateGameBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateGameFragment : Fragment() {
    private var _binding: FragmentCreateGameBinding? = null
    private val binding get() = _binding!!

    // View model setup for recycler view
    private val playerListViewModel by viewModels<PlayerListViewModel> {
        PlayerListViewModelFactory(context)
    }

    // Navigation
    private lateinit var navController: NavController

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        // Player recycler view
        val playerAdapter = PlayerAdapter{player -> adapterOnClick(player)}
        val recyclerView: RecyclerView = binding.playerRecyclerView
        recyclerView.adapter = playerAdapter

        // observe view model
        playerListViewModel.playerLiveData.observe(viewLifecycleOwner, {
            it?.let {
                playerAdapter.submitList(it as MutableList<Player>)
            }
        })

    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(player: Player) {

    }

    companion object {
        const val TAG = "CreateGameFragment"
    }
}