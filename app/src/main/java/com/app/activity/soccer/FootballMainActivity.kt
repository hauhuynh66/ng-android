package com.app.activity.soccer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.app.fragment.soccer.FootballFixtureFragment
import com.app.fragment.soccer.FootballStandingFragment
import com.app.fragment.soccer.FootballStatsFragment
import com.app.ngn.R
import com.app.util.ViewUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FootballMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_viewpager)

        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = FootballFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(ViewUtils.Companion.ZoomOutPageTransformer())
        TabLayoutMediator(tabs, pager){ tab, pos ->
            run{
                when(pos){
                    0->{
                        tab.text = "1"
                    }
                    1->{
                        tab.text = "2"
                    }
                    2->{
                        tab.text = "3"
                    }
                    3->{
                        tab.text = "4"
                    }
                }
            }
        }.attach()
    }

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
}