package com.play.musicplayerzx.fregment.home

import com.play.musicplayerzx.AudioFile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.R


class ArtistDAdapter(private val audioListR: List<AudioFile>, private val onItemClick: (position: Int) -> Unit) : RecyclerView.Adapter<ArtistDAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artist_d, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val audio = audioListR[position]
        holder.bind(audio)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return audioListR.size
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitleTextView: TextView = itemView.findViewById(R.id.artistdsongTitle)

        fun bind(audio: AudioFile) {
            songTitleTextView.text = audio.title
            // Bind other data here if needed
        }
    }
}


