package com.example.dungeonsanddragons.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dungeonsanddragons.dashboard.data.Game
import com.example.dungeonsanddragons.dashboard.data.GameSource
import com.example.dungeonsanddragons.dashboard.data.Player
import com.example.dungeonsanddragons.dashboard.model.*
import com.example.dungeonsanddragons.databinding.FragmentCreateGameBinding
import kotlin.random.Random

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

        // observe view player model
        playerListViewModel.playerLiveData.observe(viewLifecycleOwner, {
            it?.let {
                playerAdapter.submitList(it as MutableList<Player>)
            }

            // Player filter
            binding.playerFilter.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d(TAG,"Player Text Changed")
                    Log.d(TAG,binding.playerFilter.text.toString())
                    // update filter
                    playerListViewModel.updateFilter(binding.playerFilter.text.toString())
                }
            })
        })
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(player: Player) {
        // TODO: add player to the new game list
        playerListViewModel.addPlayerToGroup(player)
    }

    companion object {
        const val TAG = "CreateGameFragment"
    }
}