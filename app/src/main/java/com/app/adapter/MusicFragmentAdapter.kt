package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.fragment.mp.AudioListFragment

class MusicFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        return AudioListFragment(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}