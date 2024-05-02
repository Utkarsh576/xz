package com.play.musicplayerzx.fregment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.play.musicplayerzx.R
import com.play.musicplayerzx.databinding.FragmentHomeBinding
import com.play.musicplayerzx.fregment.home.AlbumFragment
import com.play.musicplayerzx.fregment.home.ArtistFragment
import com.play.musicplayerzx.fregment.home.FolderFragment
import com.play.musicplayerzx.fregment.home.SongFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val viewPager: ViewPager = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        // Create and set up the PagerAdapter
        val pagerAdapter = PagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager)

        return rootView
    }

    // Inner class for the PagerAdapter
    private inner class PagerAdapter(fm: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fm) {

        // This method returns the fragment for each tab
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SongFragment() // Fragment for "Song"
                1 -> AlbumFragment() // Fragment for "Album"
                2 -> ArtistFragment() // Fragment for "Artist"
                3 -> FolderFragment() // Fragment for "Folder"
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }

        // This method returns the number of tabs
        override fun getCount(): Int {
            return 4
        }

        // This method returns the title of each tab
        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Song"
                1 -> "Album"
                2 -> "Artist"
                3 -> "Folder"
                else -> null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
