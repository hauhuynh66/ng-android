package com.app.task

import android.content.ContentResolver
import com.app.data.FileData
import com.app.util.Resolver.Companion.getAudioFileData
import com.app.util.Resolver.Companion.getDownloadFileData
import com.app.util.Resolver.Companion.getImageFileData
import com.app.util.Resolver.Companion.getVideoFileData
import java.util.concurrent.Callable

class FileDataCallable(private val contentResolver: ContentResolver, private val type : Int) : Callable<ArrayList<FileData>> {
    override fun call(): ArrayList<FileData> {
        return when(type){
            1->{
                getImageFileData(contentResolver)
            }
            2->{
                getVideoFileData(contentResolver)
            }
            3->{
                getAudioFileData(contentResolver)
            }
            else->{
                getDownloadFileData(contentResolver)
            }
        }
    }
}