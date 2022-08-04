package com.app.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.app.data.AudioData

class Resolver {
    companion object {
        @SuppressLint("Range")
        fun getInternalAudioList(resolver: ContentResolver) : ArrayList<AudioData>{
            val list = arrayListOf<AudioData>()
            val musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
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
                    list.add(AudioData(title, duration, album, artist, uri))
                }
            }
            return list
        }

        @SuppressLint("Range")
        fun getExternalAudioList(resolver: ContentResolver) : ArrayList<AudioData>{
            val list = arrayListOf<AudioData>()
            val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
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
                    list.add(AudioData(title, duration, album, artist, uri))
                }
            }
            return list
        }

        fun getAudioList(resolver: ContentResolver) : ArrayList<AudioData>{
            val list1 = getInternalAudioList(resolver)
            val list2 = getExternalAudioList(resolver)
            list1.addAll(list2)
            return list1
        }
    }
}