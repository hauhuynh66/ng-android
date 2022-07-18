package com.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat

private const val MY_MEDIA_ROOT_ID = "media_root_id"

class MediaPlaybackService : MediaBrowserServiceCompat(), MediaPlayer.OnPreparedListener{
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
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
                mediaSession!!.isActive = true
                player.start()
                registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
            }
        }

        override fun onPause() {
            super.onPause()
        }

        override fun onStop() {
            super.onStop()
            unregisterReceiver(myNoisyAudioStreamReceiver)
        }
    }

    private fun getMusicList(){
        val list = arrayListOf<MusicInformation>()
        val musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        val resolver = contentResolver
        val cursor = resolver.query(musicUri,
            arrayOf(
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA
            ),
            null, null, null)
        with(cursor!!){
            while (this.moveToNext()){
                val title = this.getString(this.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE))
                val artist = this.getString(this.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
                val album = this.getString(this.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
                val duration = this.getLong(this.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
                val uri = Uri.parse(this.getString(this.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)))
                list.add(MusicInformation(title, duration, album, artist, uri))
            }
        }
        println(list.size)
    }

    override fun onCreate() {
        super.onCreate()
        getMusicList()
        player = MediaPlayer()
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
        return MediaBrowserServiceCompat.BrowserRoot(MY_MEDIA_ROOT_ID, null)
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

    class MusicInformation(val title: String, val duration: Long, val album: String, val artist: String, val uri : Uri)
}