package com.beastsandbards.android.dashboard.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beastsandbards.android.R
import com.beastsandbards.android.dashboard.data.Player

class PlayerAdapter (private val onCLick: (Player) -> Unit):
    androidx.recyclerview.widget.ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback) {

    class PlayerViewHolder(itemView: View, val onCLick: (Player) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val playerTextView : TextView = itemView.findViewById(R.id.playerText)
        private var currentPlayer: Player? = null

        init {
            itemView.setOnClickListener{
                currentPlayer?.let {
                    onCLick(it)
                }
            }
        }

        // Bind data to view
        fun bind(player: Player) {
            currentPlayer = player
            playerTextView.text = player.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return PlayerViewHolder(view, onCLick)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = getItem(position)
        holder.bind(player)
    }

    override fun submitList(list: MutableList<Player>?) {
        super.submitList(list)
    }
}

object PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.userId == newItem.userId && oldItem.charId == newItem.charId
    }
}
