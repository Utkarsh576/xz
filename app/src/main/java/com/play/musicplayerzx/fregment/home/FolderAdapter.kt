package com.play.musicplayerzx.fregment.home



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.AudioFolder
import com.play.musicplayerzx.R


class FolderAdapter(
    private val context: Context,
    private val audioFolderList: ArrayList<AudioFolder>,
    private val itemClickListener: OnFolderItemClickListener // Step 1: Interface for item click
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val folderName: TextView = itemView.findViewById(R.id.folderName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Step 2: Pass the clicked position to the click listener
            itemClickListener.onItemClick(adapterPosition)
        }
    }

    interface OnFolderItemClickListener { // Step 1: Interface for item click
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_item, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = audioFolderList[position]
        holder.folderName.text = folder.folderName
    }

    override fun getItemCount(): Int {
        return audioFolderList.size
    }
}

