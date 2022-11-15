package com.musicplayer

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.data.MediaData
import com.app.ngn.R


class Audio(
    title : String,
    val artist : String,
    val duration : Long,
    uri : Uri
) : MediaData(title, uri){
    companion object{
        private val projections = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        fun buildMetadata(audio: Audio): MediaMetadataCompat {
            return MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, audio.name)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration)
                .build()
        }

        fun buildMediaDescriptor(audio: Audio) : MediaBrowserCompat.MediaItem{
            val des = MediaDescriptionCompat.Builder()
                .setMediaId(audio.name)
                .setMediaUri(audio.uri)
                .setTitle(audio.name)
                .setSubtitle(audio.artist).build()
            return MediaBrowserCompat.MediaItem(des, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
        }

        fun getInternalAudioList(context: Context): List<Audio> {
            val audioList = mutableListOf<Audio>()
            val contentResolver = context.contentResolver

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                projections, null, null, null
            )

            if(cursor != null){
                while (cursor.moveToNext()){
                    val item = Audio(
                        cursor.getString(cursor.getColumnIndexOrThrow(projections[0])),
                        cursor.getString(cursor.getColumnIndexOrThrow(projections[1])),
                        cursor.getLong(cursor.getColumnIndexOrThrow(projections[2])),
                        Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(projections[3])))
                    )
                    audioList.add(item)
                }

            }

            cursor?.close()

            return audioList
        }

        fun getExternalAudioList(context: Context): List<Audio> {
            val audioList = mutableListOf<Audio>()
            val contentResolver = context.contentResolver

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projections, null, null, null
            )

            if(cursor != null){
                while (cursor.moveToNext()){
                    val item = Audio(
                        cursor.getString(cursor.getColumnIndexOrThrow(projections[0])),
                        cursor.getString(cursor.getColumnIndexOrThrow(projections[1])),
                        cursor.getLong(cursor.getColumnIndexOrThrow(projections[3])),
                        Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(projections[4])))
                    )
                    audioList.add(item)
                }

            }

            cursor?.close()

            return audioList
        }
    }
}

class AudioManager(data : List<Audio>) : ListManager<Audio>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(data : Audio){
            itemView.findViewById<TextView>(R.id.text).apply {
                text = data.name
            }
        }
    }
}