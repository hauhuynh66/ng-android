package com.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.fragment.sport.FootballFixtureFragment
import com.app.fragment.sport.FootballStandingFragment
import com.app.fragment.sport.FootballStatsFragment

class FootballFragmentAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm, lc) {
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
                FootballStatsFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}