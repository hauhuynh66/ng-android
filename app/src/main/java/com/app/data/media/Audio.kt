package com.app.data.media

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat

class Audio(
    title : String,
    uri: Uri,
    val artist : String,
    val album : String,
    val duration: Long
) : Media(title, uri) {
    companion object {
        private val projections = arrayOf(
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DURATION
        )

        fun getInternalAudio(resolver: ContentResolver) : List<Audio>{
            val data = mutableListOf<Audio>()
            val uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            val cursor = resolver.query(uri, projections, null, null, null)

            if(cursor != null){
                while (cursor.moveToNext()){
                    data.add(
                        Audio(
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[0])),
                            Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(projections[1]))),
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[2])),
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[3])),
                            cursor.getLong(cursor.getColumnIndexOrThrow(projections[4]))
                        )
                    )
                }
            }
            cursor?.close()

            return data
        }

        fun getExternalAudio(resolver: ContentResolver) : List<Audio>{
            val data = mutableListOf<Audio>()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor = resolver.query(uri, projections, null, null, null)

            if(cursor != null){
                while (cursor.moveToNext()){
                    data.add(
                        Audio(
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[0])),
                            Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(projections[1]))),
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[2])),
                            cursor.getString(cursor.getColumnIndexOrThrow(projections[3])),
                            cursor.getLong(cursor.getColumnIndexOrThrow(projections[4]))
                        )
                    )
                }
            }
            cursor?.close()

            return data
        }

        fun getDescription(audio: Audio) : MediaDescriptionCompat{
            val builder = MediaDescriptionCompat.Builder()
                .setTitle(audio.name)
                .setMediaUri(audio.uri)
                .setSubtitle(audio.artist)
            return builder.build()
        }

        fun getMetaData(audio: Audio) : MediaMetadataCompat{
            val builder = MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, audio.name)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, audio.album)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration)

            return builder.build()
        }
    }
}