package com.app.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.fragment.ex.EXGroupFragment
import com.app.fragment.ex.EXListFragment
import com.google.android.material.tabs.TabLayout

class ExplorerFragmentAdapter(fm: FragmentManager, val tab: TabLayout) : FragmentPagerAdapter(fm) {
    private val count = 2
    override fun getCount(): Int {
        return count;
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            1->{
                EXListFragment(object : EXListFragment.Listener{
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
                EXGroupFragment()
            }
        }

    }
}