package com.play.musicplayerzx.fregment.home

import com.play.musicplayerzx.MainActivity


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.R


class ArtistFragment : Fragment(), ArtistAdapter.OnArtistItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView from the layout
        val recyclerView: RecyclerView = view.findViewById(R.id.artistRv)

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set adapter
        val adapter = ArtistAdapter(requireContext(), MainActivity.artistList, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int, artistList: List<String>) {
        Toast.makeText(requireContext(), "Clicked on artist: ${artistList[position]}", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), ArtistD::class.java)
        intent.putExtra("position", position)

        startActivity(intent)
    }
}
