package com.app.activity.mp

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.MusicFragmentAdapter
import com.app.ngn.R
import com.app.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MusicBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mp_browser)
        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = MusicFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(Utils.Companion.ZoomOutPageTransformer())
        TabLayoutMediator(tabs, pager){
            tab, pos -> run{
                tab.view.minimumWidth = 300
                when(pos){
                    0->{
                        tab.text = "SONG"
                    }
                    1->{
                        tab.text = "ALBUM"
                    }
                    2->{
                        tab.text = "ARTIST"
                    }
                    3->{
                        tab.text = "FOLDER"
                    }
                    else->{

                    }
                }
            }
        }.attach()

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

        findViewById<ImageView>(R.id.search).apply {
            setOnClickListener{
                val intent = Intent(this@MusicBrowserActivity, MusicSearchActivity::class.java)
                startActivity(intent)
            }
        }
    }
}