package com.app.service

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.app.data.media.Audio

class MyBrowserService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener {
    private var mediaSession : MediaSessionCompat? = null
    private lateinit var stateBuilder : PlaybackStateCompat.Builder
    private var prepared : Boolean = false
    private var current: Int = 0
    private var isPlaying: Boolean = false

    private val sessionCallback : MediaSessionCompat.Callback = object : MediaSessionCompat.Callback(){
        override fun onPlay() {
            if(prepared){
                mediaPlayer.start()
                val state = PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer.currentPosition.toLong(), mediaPlayer.playbackParams.speed)
                    .build()
                mediaSession?.apply {
                    setPlaybackState(state)
                }
                isPlaying = true
            }
        }

        override fun onPause() {
            if(prepared){
                mediaPlayer.pause()

                val state = PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.currentPosition.toLong(), mediaPlayer.playbackParams.speed)
                    .build()

                mediaSession?.apply {
                    setPlaybackState(state)
                }
                isPlaying = false
            }
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            if(current < audio.size - 1){
                current++
            } else {
                current = 0
            }
            isPlaying = mediaPlayer.isPlaying

            resetMediaPlayer(audio[current])
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            if(current > 0){
                current--
            } else {
                current = audio.size - 1
            }
            isPlaying = mediaPlayer.isPlaying

            resetMediaPlayer(audio[current])
        }
    }

    private var audio = listOf<Audio>()
    private val mediaPlayer : MediaPlayer = MediaPlayer()

    private val LOG_TAG = "MY MEDIA BROWSER SERVICE"
    private val ROOT_ID = "ROOT"
    private val EMPTY_ROOT_ID = "EMPTY"

    override fun onCreate() {
        super.onCreate()
        audio = Audio.getInternalAudio(baseContext.contentResolver)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setDataSource(audio[0].uri.path)
        mediaPlayer.prepareAsync()
        mediaSession = MediaSessionCompat(this, LOG_TAG).apply {
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY)

            setPlaybackState(stateBuilder.build())
            setMetadata(Audio.getMetaData(audio[current]))
            setCallback(sessionCallback)
            setSessionToken(sessionToken)
            isActive = true
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            isActive = false
            release()
        }
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaList = mutableListOf<MediaBrowserCompat.MediaItem>()
        if(parentId != EMPTY_ROOT_ID){
            audio.forEach {
                mediaList.add(
                    MediaBrowserCompat.MediaItem(Audio.getDescription(it), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
                )
            }
        }
        result.sendResult(mediaList)
    }

    override fun onPrepared(p0: MediaPlayer?) {
        prepared = true

        if(isPlaying){
            mediaPlayer.start()
            val state = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer.currentPosition.toLong(), mediaPlayer.playbackParams.speed)
                .build()
            mediaSession?.setPlaybackState(state)
        }
    }

    private fun resetMediaPlayer(src : Audio){
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setDataSource(src.uri.path)
        mediaPlayer.prepareAsync()

        val state = PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.currentPosition.toLong(), mediaPlayer.playbackParams.speed)
            .build()
        mediaSession?.apply {
            setMetadata(Audio.getMetaData(src))
            setPlaybackState(state)
        }
    }
}