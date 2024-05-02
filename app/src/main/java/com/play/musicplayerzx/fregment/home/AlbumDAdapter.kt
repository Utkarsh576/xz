package com.play.musicplayerzx.fregment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.R


class AlbumDAdapter(
    private val context: Context,
    private val audioListA: ArrayList<AudioFile>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<AlbumDAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitleTextView: TextView = itemView.findViewById(R.id.dsongTitle)
        val songArtImageView: ImageView = itemView.findViewById(R.id.dSongArt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_item, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val audioFile = audioListA[position]
        holder.songTitleTextView.text = audioFile.title
        Glide.with(context)
            .load(audioFile.albumArtUri)
            .placeholder(R.drawable.musicplayer)
            .error(R.drawable.musicplayer)
            .into(holder.songArtImageView)

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = audioListA.size
}