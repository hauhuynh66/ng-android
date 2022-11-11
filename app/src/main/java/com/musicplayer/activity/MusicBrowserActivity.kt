package com.musicplayer.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.app.ngn.R
import com.app.util.ViewUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.musicplayer.fragment.SongListFragment

class MusicBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mp_browser)
        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = MusicFragmentAdapter(supportFragmentManager, lifecycle)
        pager.setPageTransformer(ViewUtils.Companion.ZoomOutPageTransformer())
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

    class MusicFragmentAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->{
                    SongListFragment()
                }
                else->{
                    Fragment()
                }
            }
        }
    }
}