package com.app.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.fragment.fex.EXGroup
import com.app.fragment.fex.EXList
import com.google.android.material.tabs.TabLayout

class EXFragmentAdapter(fm: FragmentManager, val tab: TabLayout) : FragmentPagerAdapter(fm) {
    private val count = 2
    override fun getCount(): Int {
        return count;
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            1->{
                EXList(object : EXList.Listener{
                    override fun onMultipleChanged(isMultiple: Boolean) {
                        if(isMultiple){
                            tab.visibility = View.GONE
                        }else{
                            tab.visibility = View.VISIBLE
                        }
                    }
                })
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