package com.play.musicplayerzx.fregment.home



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.R


class ArtistAdapter(
    private val context: Context,
    private val artistList: List<String>,
    private val itemClickListener: OnArtistItemClickListener
) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    interface OnArtistItemClickListener {
        fun onItemClick(position: Int, artistList: List<String>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_item, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.artistNameTextView.text = artistList[position]
        // Set click listener
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position, artistList)
        }
    }

    override fun getItemCount() = artistList.size

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistNameTextView: TextView = itemView.findViewById(R.id.artistName)
    }
}
