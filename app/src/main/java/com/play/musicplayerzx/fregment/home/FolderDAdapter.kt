package com.play.musicplayerzx.fregment.home


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.R


class FolderDAdapter(
    private val context: Context,
    private val audioListf: ArrayList<AudioFile>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<FolderDAdapter.FolderDViewHolder>() {

    inner class FolderDViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val songTitle: TextView = itemView.findViewById(R.id.folderdsongTitle)
        val songArtImageView: ImageView = itemView.findViewById(R.id.folderdSongArt)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderDViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folde_ritem_d, parent, false)
        return FolderDViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderDViewHolder, position: Int) {
        val audioFile = audioListf[position]
        holder.songTitle.text = audioFile.title
        Glide.with(context)
            .load(audioFile.albumArtUri)
            .placeholder(R.drawable.musicplayer)
            .error(R.drawable.musicplayer)
            .into(holder.songArtImageView)
    }

    override fun getItemCount(): Int {
        return audioListf.size
    }
}
