package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.fragment.EXGroup
import com.app.fragment.EXList

class EXFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val count = 2
    override fun getCount(): Int {
        return count;
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            1->{
                EXList()
            }
            else->{
                EXGroup()
            }
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            1->{
                "Files"
            }
            else->{
                "Groups"
            }
        }
    }
}