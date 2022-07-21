package com.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.app.data.AudioData
import com.app.model.AppDatabase
import com.app.util.Resolver.Companion.getInternalAudioList


class MediaPlaybackService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener{
    private val MY_MEDIA_ROOT_ID = "media_root_id"
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var musicList: ArrayList<AudioData>
    private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private lateinit var db: AppDatabase
    private var currentPlaying : String? = null
    private val myNoisyAudioStreamReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

        }
    }
    private lateinit var player : MediaPlayer
    private val afChangeListener = AudioManager.OnAudioFocusChangeListener {
        println(it)
    }
    private val sessionCallback = object : MediaSessionCompat.Callback(){
        override fun onPlay() {
            super.onPlay()
            val am = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setOnAudioFocusChangeListener(afChangeListener)
                setAudioAttributes(AudioAttributes.Builder().run {
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                build()
            }
            val result = am.requestAudioFocus(audioFocusRequest)
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                startService(Intent(applicationContext, MediaBrowserService::class.java))
                player.start()
                mediaSession!!.apply {
                    isActive = true
                    setPlaybackState(PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING,
                        player.currentPosition.toLong(), player.playbackParams.speed).build())
                }
                registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
            }
        }

        override fun onPause() {
            super.onPause()
            player.pause()
            mediaSession!!.setPlaybackState(PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED,
                player.currentPosition.toLong(), player.playbackParams.speed).build())
        }

        override fun onStop() {
            super.onStop()
            player.stop()
            mediaSession!!.setPlaybackState(PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_STOPPED,
                player.currentPosition.toLong(), player.playbackParams.speed).build())
            unregisterReceiver(myNoisyAudioStreamReceiver)
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
        }
    }

    override fun onCreate() {
        super.onCreate()
        musicList = getInternalAudioList(contentResolver)
        player = MediaPlayer()
        player.setDataSource(musicList[0].uri.path)
        player.prepareAsync()
        mediaSession = MediaSessionCompat(baseContext, "MEDIA_TAG").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            setCallback(sessionCallback)

            setSessionToken(sessionToken)
        }

    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = arrayListOf<MediaBrowserCompat.MediaItem>()

        if (MY_MEDIA_ROOT_ID == parentId) {

        } else {

        }

        result.sendResult(mediaItems)
    }

    override fun onPrepared(p0: MediaPlayer?) {

    }
}