package com.app.service

import android.media.MediaPlayer

class StatusPlaybackService() {
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    fun init(){
        mediaPlayer.prepareAsync()
    }

    fun destroy(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    enum class Status{
        Error,
        Warning,
        Success,
        Info
    }

    companion object {

    }
}