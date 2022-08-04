package com.app.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.app.data.AudioData
import com.app.util.Resolver.Companion.getInternalAudioList


class MediaPlaybackService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    private val MY_MEDIA_ROOT_ID = "media_root_id"
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var musicList: ArrayList<AudioData>
    private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private var currentPlaying : Int = 0
    private lateinit var previousState : PlaybackStateCompat

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
                val current = musicList[currentPlaying]
                mediaSession!!.apply {
                    isActive = true
                    val state = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING,
                        player.currentPosition.toLong(), player.playbackParams.speed).build()
                    previousState = state
                    setPlaybackState(state)
                    val metadata = MediaMetadataCompat.Builder()
                        .putText(MediaMetadataCompat.METADATA_KEY_TITLE, current.title)
                        .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, current.artist)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, current.duration)
                        .build()
                    setMetadata(metadata)
                }
                registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
            }
        }

        override fun onPause() {
            super.onPause()
            player.pause()
            val state = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED,
                player.currentPosition.toLong(), player.playbackParams.speed).build()
            previousState = state
            mediaSession!!.setPlaybackState(state)
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

            if(currentPlaying < musicList.size){
                currentPlaying ++
            }else{
                currentPlaying = 0
            }

            val current = musicList[currentPlaying]
            mediaSession!!.apply {
                isActive = true
                setPlaybackState(PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT,
                    player.currentPosition.toLong(), player.playbackParams.speed).build())
                val metadata = MediaMetadataCompat.Builder()
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, current.title)
                    .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, current.artist)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, current.duration)
                    .build()
                setMetadata(metadata)
            }
            player.stop()
            player.reset()
            player.setDataSource(musicList[currentPlaying].uri.path)
            player.prepare()

            if(previousState.state == PlaybackStateCompat.STATE_PLAYING){
                player.start()
            }
            mediaSession!!.setPlaybackState(
                PlaybackStateCompat.Builder().setState(previousState.state, 0, player.playbackParams.speed).build())
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            if(currentPlaying > 0){
                currentPlaying --
            }else{
                currentPlaying = musicList.size - 1
            }

            val current = musicList[currentPlaying]
            mediaSession!!.apply {
                isActive = true
                setPlaybackState(PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS,
                    player.currentPosition.toLong(), player.playbackParams.speed).build())
                val metadata = MediaMetadataCompat.Builder()
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, current.title)
                    .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, current.artist)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, current.duration)
                    .build()
                setMetadata(metadata)
            }
            player.stop()
            player.reset()
            player.setDataSource(musicList[currentPlaying].uri.path)
            player.prepare()

            if(previousState.state == PlaybackStateCompat.STATE_PLAYING){
                player.start()
            }
            mediaSession!!.setPlaybackState(
                PlaybackStateCompat.Builder().setState(previousState.state, 0, player.playbackParams.speed).build())
        }


    }

    override fun onCreate() {
        super.onCreate()
        musicList = getInternalAudioList(contentResolver)
        player = MediaPlayer()
        player.setOnCompletionListener(this)
        player.setOnPreparedListener(this)
        player.setDataSource(musicList[currentPlaying].uri.path)
        player.prepareAsync()

        previousState = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 0, player.playbackParams.speed).build()
        mediaSession = MediaSessionCompat(baseContext, "MEDIA_TAG").apply {
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
            val metadata = MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, musicList[currentPlaying].title)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, musicList[currentPlaying].artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, musicList[currentPlaying].duration)
                .build()
            setMetadata(metadata)
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

    @SuppressLint("WrongConstant")
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = arrayListOf<MediaBrowserCompat.MediaItem>()
        for((c, i) in musicList.withIndex()){
            val des = MediaDescriptionCompat.Builder()
                .setMediaId(i.title)
                .setMediaUri(i.uri)
                .setTitle(i.title)
                .setSubtitle(i.artist).build()
            val item : MediaBrowserCompat.MediaItem = MediaBrowserCompat.MediaItem(des, MediaBrowser.MediaItem.FLAG_BROWSABLE)
            mediaItems.add(c, item)
        }

        result.sendResult(mediaItems)
    }

    override fun onPrepared(p0: MediaPlayer?) {

    }

    override fun onCompletion(p0: MediaPlayer?) {

    }
}