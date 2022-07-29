package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.fragment.mp.AudioListFragment

class MPFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return AudioListFragment(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}