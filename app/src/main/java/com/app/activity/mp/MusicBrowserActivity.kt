package com.app.activity.mp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.app.adapter.MPFragmentAdapter
import com.app.ngn.R
import com.app.util.Utils
import com.google.android.material.tabs.TabLayout

class MusicBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mp_browser)
        val pager = findViewById<ViewPager>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = MPFragmentAdapter(supportFragmentManager)
        pager.setPageTransformer(true, Utils.Companion.ZoomOutPageTransformer())
        tabs.setupWithViewPager(pager)

        val mp = findViewById<ConstraintLayout>(R.id.player_fm)
        mp.setOnClickListener {
            val mpIntent = Intent(this, MusicPlayerActivity::class.java)
            val option = ActivityOptions.makeSceneTransitionAnimation(
                this,
                    Pair(findViewById(R.id.player_fm), "player")
            )
            startActivity(mpIntent, option.toBundle())
        }
    }
}