package com.app.activity.sport

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.FootballFragmentAdapter
import com.app.ngn.R
import com.app.util.ViewUtils
import com.app.viewmodel.Football
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FootballMainActivity : AppCompatActivity() {
    private val model : Football by viewModels()
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
}