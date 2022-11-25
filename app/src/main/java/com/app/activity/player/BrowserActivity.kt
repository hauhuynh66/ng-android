package com.app.activity.player

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.CustomListAdapter
import com.app.data.media.Audio
import com.app.data.media.AudioManager
import com.app.ngn.R
import com.app.service.MyBrowserService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BrowserActivity : AppCompatActivity() {
    private lateinit var mediaBrowser : MediaBrowserCompat
    private val connectionCallback : MediaBrowserCompat.ConnectionCallback = object : MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@BrowserActivity, token)
                MediaControllerCompat.setMediaController(this@BrowserActivity, mediaController)
            }
            buildTransportControl()
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mp_browser)
        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = BrowserAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tabs, pager){ tab, pos ->
            run {
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
                }
            }
        }.attach()

        findViewById<ConstraintLayout>(R.id.player_fm).setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }

        mediaBrowser = MediaBrowserCompat(this, ComponentName(this, MyBrowserService::class.java), connectionCallback, null)
        mediaBrowser.connect()
    }

    inner class BrowserAdapter(fm : FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc){
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->{
                    SongList()
                }
                else->{
                    Fragment()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser.disconnect()
    }

    private fun buildTransportControl(){

    }

    class SongList : Fragment(){
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val data = Audio.getInternalAudio(requireContext().contentResolver)
            val list = view.findViewById<RecyclerView>(R.id.item_list)
            list.adapter = CustomListAdapter(AudioManager(data))
            list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            return inflater.inflate(R.layout.fg_list, container, false)
        }
    }
}