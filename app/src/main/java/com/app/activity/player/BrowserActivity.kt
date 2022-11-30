package com.app.activity.player

import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.app.adapter.CustomListAdapter
import com.app.adapter.ListManager
import com.app.data.media.Audio
import com.app.data.media.AudioListManager
import com.app.ngn.R
import com.app.service.MyBrowserService
import com.app.viewmodel.AudioListConnector
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BrowserActivity : AppCompatActivity() {
    private val audioListConnector : AudioListConnector by viewModels()
    private lateinit var mediaBrowser : MediaBrowserCompat
    private var mediaMetadata : MediaMetadataCompat? = null
    private val connectionCallback : MediaBrowserCompat.ConnectionCallback = object : MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
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

    private val controllerCallback : MediaControllerCompat.Callback = object : MediaControllerCompat.Callback(){
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            updatePlaybackState(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            updateMetadata(metadata)
        }
    }
    private lateinit var mediaTitle : TextView
    private lateinit var mediaSubtitle : TextView
    private lateinit var mediaPlay : ImageView

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

        val mediaPlayer = findViewById<ConstraintLayout>(R.id.player_fm)
        mediaPlayer.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            val option = ActivityOptions.makeSceneTransitionAnimation(this, it, "player")
            startActivity(intent, option.toBundle())
        }
        mediaTitle = mediaPlayer.findViewById(R.id.audio_title)
        mediaSubtitle = mediaPlayer.findViewById(R.id.audio_subtitle)
        mediaPlay = mediaPlayer.findViewById(R.id.btn_play)

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
        MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    private fun buildTransportControl(){
        val controller = MediaControllerCompat.getMediaController(this)

        updatePlaybackState(controller.playbackState)
        updateMetadata(controller.metadata)

        mediaPlay.setOnClickListener {
            val state = controller.playbackState
            if(state.state == PlaybackStateCompat.STATE_PLAYING){
                controller.transportControls.pause()
            }else{
                controller.transportControls.play()
            }
        }

        controller.registerCallback(controllerCallback)

        audioListConnector.selectedAudio.observe(this){
            if(it != null){
                mediaController.transportControls.skipToQueueItem(it.id)
            }
        }
    }

    private fun updatePlaybackState(state : PlaybackStateCompat?){
        when(state?.state){
            PlaybackStateCompat.STATE_PLAYING->{
                mediaPlay.setImageResource(R.drawable.ic_baseline_pause)
            }
            else->{
                mediaPlay.setImageResource(R.drawable.ic_baseline_play_arrow)
            }
        }
    }

    private fun updateMetadata(metadata : MediaMetadataCompat?){
        mediaMetadata = metadata
        metadata?.apply {
            mediaTitle.text = this.getText(MediaMetadataCompat.METADATA_KEY_TITLE)
            mediaSubtitle.text = this.getText(MediaMetadataCompat.METADATA_KEY_ALBUM)
        }
    }

    class SongList : Fragment(){
        private val audioListConnector : AudioListConnector by activityViewModels()
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val data = Audio.getInternalAudio(requireContext().contentResolver)
            val list = view.findViewById<RecyclerView>(R.id.item_list)
            val audioListManager = AudioListManager(data)
            audioListManager.onItemClickListener = object : ListManager.OnItemClickListener<Audio>{
                override fun execute(item: Audio) {
                    audioListConnector.selectedAudio.value = item
                }
            }

            list.adapter = CustomListAdapter(audioListManager)
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