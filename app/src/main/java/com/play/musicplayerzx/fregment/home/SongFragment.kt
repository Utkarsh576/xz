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
import com.play.musicplayerzx.MainActivity
import com.play.swarsangam.fregmentnav.musicfregment.songsfregment.SongAdapter


class SongFragment : Fragment(), SongAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView from the layout
        val recyclerView: RecyclerView = view.findViewById(R.id.SongRv)

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set adapter
        val adapter = SongAdapter(requireContext(), MainActivity.audioList, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        // Open PlayerActivity with the clicked item's position
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}
