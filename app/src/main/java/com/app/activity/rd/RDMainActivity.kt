package com.app.activity.rd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.RDFragmentAdapter
import com.app.ngn.R
import com.app.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RDMainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_viewpager)
        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = RDFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(Utils.Companion.ZoomOutPageTransformer())
        TabLayoutMediator(tabs, pager){ tab, pos ->
            run{
                tab.view.minimumWidth = 300
                when(pos){
                    0->{
                        tab.text = "HOME"
                    }
                    1->{
                        tab.text = "POPULAR"
                    }
                }
            }
        }.attach()
    }
}