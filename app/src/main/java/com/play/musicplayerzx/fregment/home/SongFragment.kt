package com.play.musicplayerzx.fregment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.play.musicplayerzx.R


import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.AudioFile
import com.play.musicplayerzx.MainActivity
import com.play.swarsangam.fregmentnav.musicfregment.songsfregment.SongAdapter


class SongFragment : Fragment(), SongAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.SongRv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SongAdapter(requireContext(), MainActivity.audioList, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    // Function to update RecyclerView with new data
    
}

