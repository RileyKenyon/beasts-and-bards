package com.example.dungeonsanddragons.dashboard.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dungeonsanddragons.R

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    private var gameCount: Int = 0

        // viewHolder for header
        class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
            private val gameNumberTextView: TextView = itemView.findViewById(R.id.game_number_text)

            fun bind(gameCount: Int) {
                gameNumberTextView.text = gameCount.toString()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(gameCount)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun updateGameCount(updatedGameCount: Int) {
        gameCount = updatedGameCount
        notifyDataSetChanged()
    }
}
