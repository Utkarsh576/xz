package com.play.swarsangam.fregmentnav.musicfregment.songsfregment

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


class SongAdapter(
    private val context: Context,
    private val songList: List<AudioFile>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val songTitleTextView: TextView = itemView.findViewById(R.id.songTitle)
        private val durationTextView: TextView = itemView.findViewById(R.id.duration)
        private val songSizeTextView: TextView = itemView.findViewById(R.id.songSize)
        private val songArt: ImageView = itemView.findViewById(R.id.SongArt)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        fun bind(song: AudioFile) {
            songTitleTextView.text = song.title
            durationTextView.text = song.duration.toString() // Assuming duration is stored as milliseconds
            songSizeTextView.text = song.size.toString() // Assuming size is stored in bytes

            // Load song art using Glide
            Glide.with(itemView)
                .load(song.albumArtUri) // Assuming artUri is the URI of the song art
                .placeholder(R.drawable.musicplayer) // Placeholder image while loading
                .error(R.drawable.musicplayer) // Error image if Glide fails to load
                .into(songArt)
        }
    }
}
