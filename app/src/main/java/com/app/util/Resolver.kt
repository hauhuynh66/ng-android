package com.app.util

import android.content.ContentResolver
import android.provider.MediaStore
import com.app.data.FileData
import java.util.*

class Resolver {
    companion object {
        fun getImageFileData(contentResolver : ContentResolver) : ArrayList<FileData> {
            val ret = arrayListOf<FileData>()
            //EXTERNAL
            val uriE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor = contentResolver.query(uriE,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATA
                ),
                null, null, null)
            with(cursor!!){
                while(this.moveToNext()){
                    val name = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "IMAGE"))
                }
            }
            //INTERNAL
            val uriI = MediaStore.Images.Media.INTERNAL_CONTENT_URI
            val cursorI = contentResolver.query(uriI,
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATA
                ),
                null, null, null)
            with(cursorI!!){
                while(this.moveToNext()){
                    val name = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "IMAGE"))
                }
            }
            return ret
        }

        fun getVideoFileData(contentResolver : ContentResolver) : ArrayList<FileData> {
            val ret = arrayListOf<FileData>()
            //EXTERNAL
            val uriE = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val cursorE = contentResolver.query(uriE,
                arrayOf(
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA
                ),
                null, null, null)
            with(cursorE!!){
                while(this.moveToNext()){
                    val name = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "Video"))
                }
            }

            //INTERNAL
            val uriI = MediaStore.Video.Media.INTERNAL_CONTENT_URI
            val cursorI = contentResolver.query(uriI,
                arrayOf(
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA
                ),
                null, null, null)
            with(cursorI!!){
                while(this.moveToNext()){
                    val name = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "Video"))
                }
            }
            return ret
        }

        fun getAudioFileData(contentResolver : ContentResolver) : ArrayList<FileData> {
            val ret = arrayListOf<FileData>()
            //EXTERNAL
            val uriE = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            val cursorE = contentResolver.query(uriE,
                arrayOf(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DATA
                ),
                null, null, null)
            with(cursorE!!){
                while(this.moveToNext()){
                    val name = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "Audio"))
                }
            }
            //INTERNAL
            val uriI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursorI = contentResolver.query(uriI,
                arrayOf(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DATA
                ),
                null, null, null)
            with(cursorI!!){
                while(this.moveToNext()){
                    val name = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "Audio"))
                }
            }
            return ret
        }

        fun getDownloadFileData(contentResolver : ContentResolver) : ArrayList<FileData> {
            val ret = arrayListOf<FileData>()
            val uriI = MediaStore.Downloads.INTERNAL_CONTENT_URI
            val cursorI = contentResolver.query(uriI,
                arrayOf(
                    MediaStore.Downloads.DISPLAY_NAME,
                    MediaStore.Downloads.SIZE,
                    MediaStore.Downloads.DATE_ADDED,
                    MediaStore.Downloads.DATA
                ),
                null, null, null)
            with(cursorI!!){
                while(this.moveToNext()){
                    val name = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorI.getLong(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorI.getString(cursorI.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "DOWNLOAD"))
                }
            }

            val uriE = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val cursorE = contentResolver.query(uriE,
                arrayOf(
                    MediaStore.Downloads.DISPLAY_NAME,
                    MediaStore.Downloads.SIZE,
                    MediaStore.Downloads.DATE_ADDED,
                    MediaStore.Downloads.DATA
                ),
                null, null, null)
            with(cursorE!!){
                while(this.moveToNext()){
                    val name = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE))
                    val date = Date(cursorE.getLong(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_ADDED)))
                    val path = cursorE.getString(cursorE.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                    ret.add(FileData(name,date, size, path, "DOWNLOAD"))
                }
            }
            return ret
        }
    }
}