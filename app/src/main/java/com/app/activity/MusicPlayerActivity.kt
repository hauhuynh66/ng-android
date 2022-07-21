package com.app.activity

import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.app.service.MediaPlaybackService

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var mediaBrowser : MediaBrowserCompat
    private lateinit var playPause : ImageButton
    private lateinit var next : ImageButton
    private lateinit var prev : ImageButton
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
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
        }

        override fun onSessionDestroyed() {
            mediaBrowser.disconnect()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_music_player)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.apply {
            title = "Music Player"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_downward)
        }
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
        val pbState = mediaController.playbackState
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
}