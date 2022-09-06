package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.fragment.sport.FootballFixtureFragment
import com.app.fragment.sport.FootballStandingFragment

class FootballTabAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                FootballFixtureFragment()
            }
            1->{
                FootballStandingFragment()
            }
            2->{
                //holder
                Fragment()
            }
            else->{
                //holder
                Fragment()
            }
        }
    }
}