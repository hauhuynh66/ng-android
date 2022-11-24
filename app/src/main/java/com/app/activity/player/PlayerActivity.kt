package com.app.activity.player

import android.content.ComponentName
import android.media.session.PlaybackState
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.app.service.MyBrowserService

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaBrowser : MediaBrowserCompat
    private lateinit var playPause : ImageButton
    private lateinit var next : ImageButton
    private lateinit var prev : ImageButton
    private lateinit var queueMode : ImageButton
    private lateinit var listDisplay : ImageButton
    private lateinit var cTitle : TextView
    private lateinit var cSubtitle : TextView

    private val connectionCallback : MediaBrowserCompat.ConnectionCallback = object : MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@PlayerActivity, token)
                MediaControllerCompat.setMediaController(this@PlayerActivity,mediaController)
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
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            super.onShuffleModeChanged(shuffleMode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_music_player)

        mediaBrowser = MediaBrowserCompat(this, ComponentName(this, MyBrowserService::class.java), connectionCallback, null)
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    fun buildTransportControl(){
        val controller = MediaControllerCompat.getMediaController(this)
        initControl()
        playPause.setOnClickListener {
            val state = mediaController.playbackState?.state
            if(state == PlaybackState.STATE_PLAYING){
                controller.transportControls.pause()
            }else{
                controller.transportControls.play()
            }
        }

        next.setOnClickListener {
            controller.transportControls.skipToNext()
        }

        prev.setOnClickListener {
            controller.transportControls.skipToPrevious()
        }

        controller?.registerCallback(controllerCallback)
    }

    private fun initControl(){
        playPause = findViewById(R.id.btn_play)
        next = findViewById(R.id.btn_next)
        prev = findViewById(R.id.btn_prev)
        queueMode = findViewById(R.id.btn_mode)
        listDisplay = findViewById(R.id.btn_list)
    }
}