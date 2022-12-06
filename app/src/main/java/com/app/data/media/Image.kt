package com.app.data.media

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.ngn.R

class Image(
    name : String,
    uri : Uri,
    size : Long
) : Media(name, uri) {

    companion object{
        private val columns = arrayOf(
            MediaStore.Images.ImageColumns.TITLE,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )
        fun getExternalImages(contentResolver: ContentResolver) : List<Image>{
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            println(uri.path)
            val cursor = contentResolver.query(uri, columns,null, null, null)
            val list = mutableListOf<Image>()
            if(cursor!=null){
                while (cursor.moveToNext()){
                    list.add(
                        Image(
                            cursor.getString(cursor.getColumnIndexOrThrow(columns[0])),
                            Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(columns[1]))),
                            cursor.getLong(cursor.getColumnIndexOrThrow(columns[2]))
                        )
                    )
                }
            }
            cursor?.close()
            return list
        }
    }
}

class ImageListManager(data : List<Image>) : ListManager<Image>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.com_image, parent, false)
        )
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position], onItemClickListener)
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(image: Image, onItemClickListener: OnItemClickListener<Image>?){
            val preview = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.uri.path), 100, 100)
            itemView.findViewById<ImageView>(R.id.image).apply {
                setImageBitmap(preview)
                if(onItemClickListener!=null){
                    setOnClickListener {
                        onItemClickListener.execute(image)
                    }
                }
            }
        }
    }
}