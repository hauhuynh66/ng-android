package com.app.activity.player

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.app.service.MyBrowserService
import com.app.util.DateTimeUtils

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaBrowser : MediaBrowserCompat
    private lateinit var playPause : ImageButton
    private lateinit var next : ImageButton
    private lateinit var prev : ImageButton
    private lateinit var queueMode : ImageButton
    private lateinit var listDisplay : ImageButton
    private lateinit var cTitle : TextView
    private lateinit var cSubtitle : TextView
    private lateinit var cBitmap : ImageView
    private lateinit var cProgress : SeekBar
    private lateinit var cTime : TextView
    private lateinit var cDuration : TextView
    private var current : Long = 0L

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
            updatePlaybackState(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            updateMetaData(metadata)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            super.onShuffleModeChanged(shuffleMode)
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowser.disconnect()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_music_player)

        mediaBrowser = MediaBrowserCompat(this, ComponentName(this, MyBrowserService::class.java), connectionCallback, null)
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

        updatePlaybackState(controller.playbackState)
        updateMetaData(controller.metadata)

        playPause.setOnClickListener {
            val state = mediaController.playbackState?.state
            if(state == PlaybackStateCompat.STATE_PLAYING){
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
        cTitle = findViewById(R.id.audio_title)
        cSubtitle = findViewById(R.id.audio_subtitle)
        cBitmap = findViewById(R.id.audio_img)
        cProgress = findViewById(R.id.audio_progress)
        cTime = findViewById(R.id.current_time)
        cDuration = findViewById(R.id.duration)
    }

    private fun updateMetaData(metadata: MediaMetadataCompat?){
        metadata?.apply {
            cTitle.text = metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE)
            cSubtitle.text = metadata.getText(MediaMetadataCompat.METADATA_KEY_ALBUM)
            cDuration.text = DateTimeUtils.timeFromLong(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION))
            cTime.text = DateTimeUtils.timeFromLong(0)
        }
    }

    private fun updatePlaybackState(playbackState: PlaybackStateCompat?){
        val iconRes = when(playbackState?.state){
            PlaybackStateCompat.STATE_PLAYING->{
                R.drawable.ic_baseline_pause
            }
            else->{
                R.drawable.ic_baseline_play_arrow
            }
        }

        playPause.setImageResource(iconRes)
        cTime.text = DateTimeUtils.timeFromLong(playbackState?.position)
    }


}