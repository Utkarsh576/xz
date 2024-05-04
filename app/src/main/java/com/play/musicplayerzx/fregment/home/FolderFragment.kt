package com.play.musicplayerzx.fregment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.R
import com.play.musicplayerzx.databinding.FragmentFolderBinding

class FolderFragment : Fragment(), FolderAdapter.OnFolderItemClickListener {
    private lateinit var binding: FragmentFolderBinding
    private lateinit var adapter: FolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set audio data change listener
        (requireActivity() as? MainActivity<Any?>)?.setAudioDataChangeListener(this)

        // Find the RecyclerView using view binding
        val recyclerView: RecyclerView = binding.folderRv

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set adapter with click listener
        adapter = FolderAdapter(requireContext(), MainActivity.audioFolderList, this)
        recyclerView.adapter = adapter
    }

    // Implement item click listener
    override fun onItemClick(position: Int) {
        // Handle item click
        val folder = MainActivity.audioFolderList[position]
        val intent = Intent(requireContext(), FolderD::class.java)
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
