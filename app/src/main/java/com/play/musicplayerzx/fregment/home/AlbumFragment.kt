package com.play.musicplayerzx.fregment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.AlbumInfo
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R



class AlbumFragment : Fragment(), AlbumAdapter.OnAlbumItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView from the layout
        val recyclerView: RecyclerView = view.findViewById(R.id.albumRv)

        // Set layout manager
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        // Set adapter with click listener
        val adapter = AlbumAdapter(requireContext(), MainActivity.albumList, this)
        recyclerView.adapter = adapter
    }

    // Implement item click listener
    override fun onItemClick(position: Int, albumList: ArrayList<AlbumInfo>) {
        // Open new activity with intent
        val intent = Intent(requireContext(), AlbumD::class.java)
        intent.putExtra("position", position)

        startActivity(intent)
    }
}