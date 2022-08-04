package com.app.activity.mp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
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

        findViewById<ConstraintLayout>(R.id.player_fm).apply {
            setOnClickListener {
                val mpIntent = Intent(this@MusicBrowserActivity, MusicPlayerActivity::class.java)
                val option = ActivityOptions.makeSceneTransitionAnimation(
                    this@MusicBrowserActivity,
                    Pair(this, "player")
                )
                startActivity(mpIntent, option.toBundle())
            }
        }

        val search = findViewById<SearchView>(R.id.search_view)
        search.setOnSearchClickListener {
            val intent = Intent(this, MusicSearchActivity::class.java)
            startActivity(intent)
        }
    }
}