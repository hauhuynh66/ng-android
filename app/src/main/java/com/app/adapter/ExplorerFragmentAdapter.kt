package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.fragment.ex.EXGroupFragment
import com.app.fragment.ex.EXListFragment

class ExplorerFragmentAdapter(fm: FragmentManager, lc : Lifecycle ) : FragmentStateAdapter(fm, lc){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                EXGroupFragment()
            }
            1->{
                EXListFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}