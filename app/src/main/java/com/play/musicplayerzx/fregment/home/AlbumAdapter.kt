package com.play.musicplayerzx.fregment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.play.musicplayerzx.AlbumInfo
import com.play.musicplayerzx.R


class AlbumAdapter(
    private val context: Context,
    private val albumList: ArrayList<AlbumInfo>,
    private val itemClickListener: AlbumFragment
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    interface OnAlbumItemClickListener {
        fun onItemClick(position: Int, albumList: ArrayList<AlbumInfo>)
    }
    // Inner ViewHolder class
    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumNameTextView: TextView = itemView.findViewById(R.id.albumName)
        val albumArt: ImageView = itemView.findViewById(R.id.albumArt)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_item, parent, false)
        return AlbumViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]
        holder.albumNameTextView.text = album.album
        Glide.with(context)
            .load(album.albumArtUri)
            .placeholder(R.drawable.musicplayer)
            .error(R.drawable.musicplayer)
            .into(holder.albumArt)

        // Set click listener
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position, albumList)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = albumList.size
}