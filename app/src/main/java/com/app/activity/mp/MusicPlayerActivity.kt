package com.app.activity.mp

import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.app.service.MediaPlaybackService

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var mediaBrowser : MediaBrowserCompat
    private lateinit var playPause : ImageButton
    private lateinit var next : ImageButton
    private lateinit var prev : ImageButton
    private lateinit var displayView : View
    private var pos : Long = 0
    private var max : Long = 0
    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@MusicPlayerActivity, token)
                MediaControllerCompat.setMediaController(this@MusicPlayerActivity, mediaController)
            }
            buildTransportControls()
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback(){
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            if(metadata!=null){
                changeLayout(displayView, metadata)
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            if(state!=null){
                changeLayout(displayView, state)
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowser.disconnect()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_music_player)
        setSupportActionBar(findViewById(R.id.toolbar))
        displayView = findViewById(R.id.group1)
        supportActionBar!!.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_downward)
        }

        findViewById<TextView>(R.id.toolbar_title).text = "Music Player"

        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, MediaPlaybackService::class.java),
            connectionCallbacks,
            null
        )
    }

    private fun buildTransportControls(){
        val mediaController = MediaControllerCompat.getMediaController(this)
        playPause = findViewById<ImageButton?>(R.id.btn_play).apply {
            setOnClickListener {
                val pbState = mediaController.playbackState.state
                if(pbState == PlaybackStateCompat.STATE_PLAYING){
                    mediaController.transportControls.pause()
                }else{
                    mediaController.transportControls.play()
                }
            }
        }

        next = findViewById<ImageButton?>(R.id.btn_next).apply {
            setOnClickListener{
                mediaController.transportControls.skipToNext()
            }
        }

        prev = findViewById<ImageButton?>(R.id.btn_prev).apply {
            setOnClickListener{
                mediaController.transportControls.skipToPrevious()
            }
        }

        val metadata = mediaController.metadata

        if(metadata!=null){
            changeLayout(displayView, metadata)
        }

        val pbState = mediaController.playbackState
        if(pbState!=null){
            changeLayout(displayView, pbState)
        }

        mediaController.registerCallback(controllerCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
        mediaBrowser.subscribe("test", object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    private fun changeLayout(mainView: View, state : PlaybackStateCompat){
        when(state.state){
            PlaybackStateCompat.STATE_PLAYING->{
                playPause.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause))
            }
            PlaybackStateCompat.STATE_PAUSED->{
                playPause.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow))

                val current = mainView.findViewById<TextView>(R.id.current_time)
                val sb = mainView.findViewById<SeekBar>(R.id.audio_progress)
                pos = state.position / 1000
                sb.progress = ((pos.toDouble()/max.toDouble()) * 100).toInt()
                current.text = convertToTime(pos)
            }
        }
    }

    private fun changeLayout(mainView : View, metadata: MediaMetadataCompat){
        val title = mainView.findViewById<TextView>(R.id.audio_title)
        val subtitle = mainView.findViewById<TextView>(R.id.audio_subtitle)
        val duration = mainView.findViewById<TextView>(R.id.duration)
        title.text = metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE)
        subtitle.text = metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST)
        this.max = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)/1000
        duration.text = convertToTime(this.max)
    }

    private fun convertToTime(m : Long) : String {
        val sb = StringBuilder()
        val n = 2
        val min: Int = m.toInt() / 60
        sb.append(min.toString().padStart(n, '0'))
        sb.append(":")
        sb.append((m.toInt() - min * 60).toString().padStart(2, '0'))
        return sb.toString()
    }
}