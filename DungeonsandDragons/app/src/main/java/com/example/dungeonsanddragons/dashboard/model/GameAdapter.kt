package com.example.dungeonsanddragons.dashboard.model

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dungeonsanddragons.R
import com.example.dungeonsanddragons.dashboard.data.Game

class GameAdapter (private val onClick: (Game) -> Unit) :
//    ListAdapter<Game, GameAdapter.GameViewHolder>(GameDiffCallback) {
    androidx.recyclerview.widget.ListAdapter<Game, GameAdapter.GameViewHolder>(GameDiffCallback) {
//    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {
    class GameViewHolder(itemView: View, val onClick: (Game) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val gameTextView: TextView = itemView.findViewById(R.id.gameText)
        private val gameStatusImageView: ImageView = itemView.findViewById(R.id.gameStatus)
        private var currentGame: Game? = null

        init {
            itemView.setOnClickListener {
                currentGame?.let {
                    onClick(it)
                }
            }
        }

        // Bind data to view
        fun bind(game: Game) {
            currentGame = game
            gameTextView.text = game.name

            // Set the game status icon if active
            if (currentGame?.active == true) {
                gameStatusImageView.setImageResource(R.drawable.ic_green_baseline_check_circle_outline_24)
            } else {
                gameStatusImageView.setImageResource(R.drawable.ic_red_baseline_not_interested_24)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }
}

object GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }
}
