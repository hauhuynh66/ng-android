package com.app.task

import android.content.ContentResolver
import android.provider.MediaStore
import com.app.data.FileData
import java.util.concurrent.Callable

class ImageQuery(private val contentResolver: ContentResolver) : Callable<ArrayList<FileData>> {
    override fun call(): ArrayList<FileData> {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri,
            arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATA
            ),
            null, null, null)
        with(cursor!!){
            while(this.moveToNext()){

            }
        }
    }
}
