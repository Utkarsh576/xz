package com.play.musicplayerzx.fregment.home



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R


class FolderFragment : Fragment(), FolderAdapter.OnFolderItemClickListener { // Step 2: Implement interface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.folderRv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FolderAdapter(requireContext(), MainActivity.audioFolderList, this) // Step 3: Pass this as click listener
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) { // Step 4: Handle item click
        val folder = MainActivity.audioFolderList[position]
        val intent = Intent(requireContext(),FolderD::class.java)
        intent.putExtra("position", position)
        // intent.putExtra("folderList", MainActivity.audioFolderList)
        startActivity(intent)
    }
}

