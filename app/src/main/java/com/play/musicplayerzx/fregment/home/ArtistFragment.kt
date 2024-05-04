package com.play.musicplayerzx.fregment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.play.musicplayerzx.MainActivity
import com.play.musicplayerzx.databinding.FragmentArtistBinding

class ArtistFragment : Fragment(), ArtistAdapter.OnArtistItemClickListener {
    private lateinit var binding: FragmentArtistBinding
    private lateinit var adapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set audio data change listener
        (requireActivity() as? MainActivity<*>)?.setAudioDataChangeListener(this)

        // Set up RecyclerView
        binding.artistRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = ArtistAdapter(requireContext(), MainActivity.artistList, this)
        binding.artistRv.adapter = adapter
    }

    override fun onItemClick(position: Int, artistList: List<String>) {
        Toast.makeText(requireContext(), "Clicked on artist: ${artistList[position]}", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), ArtistD::class.java)
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
