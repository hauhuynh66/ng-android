package com.app.data.media

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaDescriptionCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager

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
    }
}

class AudioManager(data : List<Audio>) : ListManager<Audio>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(audio: Audio){
            itemView.findViewById<TextView>(android.R.id.text1).apply {
                text = audio.name
            }
        }
    }
}