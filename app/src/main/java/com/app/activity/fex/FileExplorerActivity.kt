package com.app.activity.fex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.adapter.EXFragmentAdapter
import com.app.ngn.R
import com.google.android.material.tabs.TabLayout

class FileExplorerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_flx)
        val pager = findViewById<ViewPager>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = EXFragmentAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)
    }
}