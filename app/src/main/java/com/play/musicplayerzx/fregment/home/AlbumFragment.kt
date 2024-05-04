package com.play.musicplayerzx.fregment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.AlbumInfo
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R
import com.play.musicplayerzx.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment(), AlbumAdapter.OnAlbumItemClickListener {
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set audio data change listener
        (requireActivity() as? MainActivity<*>)?.setAudioDataChangeListener(this)

        // Find the RecyclerView using view binding
        val recyclerView: RecyclerView = binding.albumRv

        // Set layout manager
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        // Set adapter with click listener
        adapter = AlbumAdapter(requireContext(), MainActivity.albumList, this)
        recyclerView.adapter = adapter
    }

    // Implement item click listener
    override fun onItemClick(position: Int, albumList: ArrayList<AlbumInfo>) {
        // Open new activity with intent
        val intent = Intent(requireContext(), AlbumD::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    // Function to update UI with new data list
    fun updateUIWithNewDataList() {
        // Update the UI with the new data list
        // For example, if you have a RecyclerView adapter, you can notify it like this:
        adapter.notifyDataSetChanged()
    }
}
